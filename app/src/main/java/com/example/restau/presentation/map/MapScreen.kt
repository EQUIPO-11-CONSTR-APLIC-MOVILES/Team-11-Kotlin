package com.example.restau.presentation.map

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.restau.domain.model.Restaurant
import com.example.restau.ui.theme.Poppins
import com.example.restau.ui.theme.radiusBlue
import com.example.restau.utils.hasLocationPermission
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    mapViewModel: MapViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val permissions = rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)


    LaunchedEffect(Unit) {
        Log.d("DONITEST", "PERMISSIONS LAUNCHED")
        Log.d("DONITEST", "PERMISSION: ${context.hasLocationPermission()}")
        Log.d("DONITEST", "PERMISSION STATE: ${mapViewModel.state.permissionsGranted}")
        permissions.launchPermissionRequest()
    }

    when {
        permissions.status == PermissionStatus.Granted -> {
            Log.d("DONITEST", "PERMISSIONS GRANTED PART")
            mapViewModel.onEvent(MapEvent.PermissionGranted)

        }

        else -> {
            Log.d("DONITEST", "PERMISSIONS REVOKED PART")
            mapViewModel.onEvent(MapEvent.PermissionRevoked)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {
        if (mapViewModel.state.permissionsGranted) {
            MapContent(
                startLocation = mapViewModel.state.startLocation,
                restaurants = mapViewModel.state.restaurants,
                circleCenter = mapViewModel.state.circleLocation,
                modifier = Modifier.padding(
                    top = it.calculateTopPadding(),
                    start = it.calculateStartPadding(LayoutDirection.Ltr),
                    end = it.calculateEndPadding(LayoutDirection.Rtl)
                )
            )
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            mapViewModel.cancelCircleFollowing()
        }
    }
}

@Composable
private fun MapContent(
    startLocation: LatLng,
    circleCenter: LatLng,
    restaurants: List<Restaurant>,
    modifier: Modifier = Modifier
) {


    val cameraState = rememberCameraPositionState()

    LaunchedEffect(startLocation) {
        cameraState.centerOnLocation(startLocation)
    }

    Box(modifier = modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraState,
            properties = MapProperties(
                isMyLocationEnabled = true
            )
        ) {
            restaurants.forEach { restaurant ->
                MarkerInfoWindow(
                    state = rememberMarkerState(
                        position = LatLng(
                            restaurant.latitude,
                            restaurant.longitude
                        )
                    )
                ) {
                    CardMarker(restaurant = restaurant)
                }
            }
            Circle(
                center = circleCenter,
                fillColor = radiusBlue,
                strokeWidth = 2f,
                radius = 100.0
            )
        }
    }
}

@Composable
fun SliderCard() {
    Card {
    }
}

@Composable
fun CardMarker(
    restaurant: Restaurant,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(19.dp),
        colors = CardDefaults.cardColors().copy(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        modifier = modifier
            .width(200.dp)
            .height(300.dp)
            .padding(10.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            Text(
                text = restaurant.name,
                fontSize = 20.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            val request = ImageRequest.Builder(LocalContext.current).data(restaurant.imageUrl).allowHardware(false).build()
            AsyncImage(
                model = request,
                contentDescription = restaurant.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .fillMaxWidth()
                    .height(100.dp)
            )

        }
    }
}

private suspend fun CameraPositionState.centerOnLocation(
    location: LatLng
) = animate(
    update = CameraUpdateFactory.newLatLngZoom(
        location,
        17f
    ),
    durationMs = 1500
)

private fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap {
    val drawable = ContextCompat.getDrawable(context, drawableId)
    val bitmap = Bitmap.createBitmap(
        drawable!!.intrinsicWidth,
        drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}