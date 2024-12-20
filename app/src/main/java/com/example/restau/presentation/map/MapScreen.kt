package com.example.restau.presentation.map

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.navigation.NavController
import com.example.restau.R
import com.example.restau.presentation.common.LoadingCircle
import com.example.restau.presentation.common.NoConnection
import com.example.restau.presentation.map.components.CardMarker
import com.example.restau.presentation.map.components.NoPermissionsSign
import com.example.restau.presentation.map.components.SliderCard
import com.example.restau.presentation.navigation.Route
import com.example.restau.ui.theme.Poppins
import com.example.restau.ui.theme.radiusBlue
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    mapViewModel: MapViewModel = hiltViewModel(),
    navController: NavController
) {

    val isConnected by mapViewModel.isConnected.collectAsState()


    if (mapViewModel.state.isLoading && !isConnected) {
        NoConnection(
            modifier = Modifier.background(Color.White)
        )
    } else if (mapViewModel.state.isLoading && !mapViewModel.locationEnabled) {
        LocationDisabled(
            modifier = Modifier.background(Color.White)
        )
    } else {

        val permissions =
            rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)
        val context = LocalContext.current

        LifecycleResumeEffect(Unit) {
            mapViewModel.onEvent(MapEvent.ScreenOpened)

            onPauseOrDispose {
                mapViewModel.onEvent(MapEvent.ScreenClosed)
            }
        }


        LaunchedEffect(Unit) {
            permissions.launchPermissionRequest()
        }

        LaunchedEffect(permissions.status) {
            when (permissions.status) {
                is PermissionStatus.Granted -> {
                    Log.d("DONITEST", "BOOM")
                    mapViewModel.onEvent(MapEvent.PermissionGranted)
                }

                is PermissionStatus.Denied -> {
                    mapViewModel.onEvent(MapEvent.PermissionDenied)
                }
            }
        }

        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { paddingValues ->
            if (mapViewModel.state.permission) {
                MapContent(
                    state = mapViewModel.state,
                    currentLocation = mapViewModel.currentLocation,
                    filteredRestaurants = mapViewModel.filteredRestaurants,
                    circleRadius = mapViewModel.circleRadius,
                    likedAndNew = mapViewModel.likedAndNew,
                    onPinClick = { index, onGather ->
                        mapViewModel.onEvent(MapEvent.PinClick(index, onGather))
                        if (mapViewModel.likedAndNew[index]) {
                            mapViewModel.onEvent(MapEvent.FeatureInteraction("special_pin_feature"))
                        }
                    },
                    onRadiusChange = {
                        mapViewModel.onEvent(MapEvent.RadiusChanged(it))
                        mapViewModel.onEvent(MapEvent.FeatureInteraction("search_radius_feature"))
                    },
                    modifier = Modifier.padding(
                        top = paddingValues.calculateTopPadding(),
                        start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                        end = paddingValues.calculateEndPadding(LayoutDirection.Rtl)
                    ),
                    onCardClick = { restaurantId ->
                        navController.navigate(Route.RestaurantScreen.route + "/${restaurantId}")
                    },
                    locationEnabled = mapViewModel.locationEnabled
                )
            } else {
                NoPermissionsSign(tryAgain = {
                    val intent = Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", context.packageName, null)
                    )
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(context, intent, null)
                })
            }
        }

        DisposableEffect(Unit) {
            onDispose {
                mapViewModel.onEvent(MapEvent.Closing)
            }
        }
    }
}

@Composable
private fun MapContent(
    state: MapState,
    filteredRestaurants: List<Boolean>,
    circleRadius: Double,
    currentLocation: LatLng,
    onPinClick: (Int, suspend () -> Unit) -> Unit,
    onRadiusChange: (Double) -> Unit,
    likedAndNew: List<Boolean>,
    onCardClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    locationEnabled: Boolean,
) {
    val cameraState = rememberCameraPositionState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val mapStyleOptions = remember {
        MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style)
    }

    LaunchedEffect(state.startLocation) {
        Log.d("DONITEST", "LAUNCHED EFFECT")
        cameraState.centerOnLocation(state.startLocation, 17f)
    }

    Box(modifier = modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraState,
            properties = MapProperties(
                isMyLocationEnabled = locationEnabled,
                mapStyleOptions = mapStyleOptions
            ),
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
                mapToolbarEnabled = false
            ),
        ) {
            if (!state.isLoading) {
                state.restaurants.forEachIndexed { index, restaurant ->
                    MarkerInfoWindow(
                        state = rememberMarkerState(key = (state.images2[index] == null).toString() ,position = LatLng(restaurant.latitude, restaurant.longitude)),
                        zIndex = Float.MAX_VALUE,
                        infoWindowAnchor = Offset(0.5f, 0.56f),
                        icon = if (!likedAndNew[index]) null else BitmapDescriptorFactory.fromResource(R.drawable.specialpin),
                        visible = filteredRestaurants[index],
                        onInfoWindowClick = {
                            onCardClick(restaurant.documentId)
                        },
                        onClick = {marker ->
                            onPinClick(index) {
                                delay(250L)
                                if (marker.isInfoWindowShown) {
                                    marker.hideInfoWindow()
                                    marker.showInfoWindow()
                                }
                            }
                            scope.launch {
                                cameraState.centerOnLocation(
                                    location = LatLng(restaurant.latitude, restaurant.longitude),
                                    zoom = 18f
                                )
                            }
                            false
                        }
                    ) {
                        CardMarker(
                            restaurant = restaurant,
                            bitmap = state.images2[index],
                        )
                    }
                }
                Circle(
                    center = currentLocation,
                    fillColor = radiusBlue,
                    strokeWidth = 0f,
                    radius = circleRadius,
                    zIndex = 0.3f
                )
            }
        }
        if (state.isLoading) {
            LoadingCircle(modifier = Modifier.align(Alignment.Center))
        } else {
            SliderCard(
                radius = circleRadius,
                onChange = onRadiusChange,
                modifier = Modifier.align(
                    Alignment.BottomCenter
                )
            )
        }
    }
}


private suspend fun CameraPositionState.centerOnLocation(
    location: LatLng,
    zoom: Float
) = animate(
    update = CameraUpdateFactory.newLatLngZoom(
        location,
        zoom
    ),
    durationMs = 1500
)

@Composable
fun LocationDisabled(
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(5.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.map),
            contentDescription = "Enable your location and start your experience!",
            tint = Color.Gray,
            modifier = Modifier.size(65.dp)
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = "Enable your location and start your experience!",
            color = Color.Gray,
            fontFamily = Poppins,
            fontSize = 25.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }
}