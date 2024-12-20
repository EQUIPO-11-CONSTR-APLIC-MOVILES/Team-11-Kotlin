package com.example.restau.presentation.search

import android.app.Activity
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.navigation.NavHostController
import com.example.restau.R
import com.example.restau.domain.model.Restaurant
import com.example.restau.presentation.common.DynamicTopBar
import com.example.restau.presentation.common.NoConnection
import com.example.restau.presentation.common.RestaurantCard
import com.example.restau.presentation.common.TopBarAction
import com.example.restau.presentation.navigation.Route
import com.example.restau.ui.theme.SoftRed

@Composable
fun SearchScreen(
    navController: NavHostController,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val restaurantName = viewModel.restaurantName
    val user = viewModel.currentUser
    val isNavigatingUserDetail = viewModel.isNavigatingUserDetail

    val isConnected by viewModel.isConnected.collectAsState()

    LaunchedEffect(isConnected) {
        viewModel.onEvent(SearchEvent.ScreenLaunched)
        viewModel.onEvent(SearchEvent.ChangeIsNavigatingUserDetail(false))
    }


    LifecycleResumeEffect(Unit) {
        viewModel.onEvent(SearchEvent.ChangeIsNavigatingUserDetail(false))

        viewModel.onEvent(SearchEvent.ScreenOpened)

        onPauseOrDispose {
            viewModel.onEvent(SearchEvent.ScreenClosed)
        }
    }

    val context = LocalContext.current
    val activity = context as? Activity

    val speechRecognizerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val spokenText = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0)
        if (!spokenText.isNullOrEmpty()) {
            viewModel.onEvent(SearchEvent.VoiceRecognitionChangeEvent(spokenText))
            viewModel.onEvent(SearchEvent.FeatureInteraction("voice_recognition_feature"))
        }
    }



    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            DynamicTopBar(
                label = {  },
                hasBackButton = false,
                action = TopBarAction.PhotoAction(
                    imageUrl = user.profilePic,
                    onPhoto = {
                        if (!isNavigatingUserDetail) {
                            viewModel.onEvent(SearchEvent.ChangeIsNavigatingUserDetail(true))
                            navController.navigate(Route.UserDetailScreen.route)
                        }
                    }
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                    end = paddingValues.calculateEndPadding(LayoutDirection.Rtl)
                )
                .padding(start = 16.dp, end = 16.dp, top = 8.dp)
                .fillMaxSize()
        ) {
            if (!viewModel.showFallback) {
                OutlinedTextField(
                    value = restaurantName,
                    onValueChange = {
                        if (it.length <= 30) {
                            viewModel.onEvent(SearchEvent.ChangeNameEvent(it))
                            viewModel.onEvent(SearchEvent.SearchFilterEvent(it))
                        }
                    },
                    label = { Text("Restaurant Name or Categories") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SoftRed,
                        focusedLabelColor = SoftRed,
                        cursorColor = SoftRed
                    ),
                    trailingIcon = {
                        Row {
                            if (restaurantName.isNotEmpty()) {
                                IconButton(onClick = {
                                    viewModel.onEvent(
                                        SearchEvent.ChangeNameEvent(
                                            ""
                                        )
                                    )
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.cleartext),
                                        contentDescription = "Clear text"
                                    )
                                }
                            }

                            IconButton(onClick = {
                                viewModel.onEvent(
                                    SearchEvent.VoiceRecognitionEvent(
                                        activity!!,
                                        speechRecognizerLauncher
                                    )
                                )
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.microphone),
                                    contentDescription = "Speak restaurant"
                                )
                            }
                        }
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (viewModel.showFallback){
                NoConnection()
            }

            else if (state.recentRestaurants.isNotEmpty() && restaurantName.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Button(onClick = { viewModel.onEvent(SearchEvent.ClearRecentRestaurantsEvent) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SoftRed
                      )
                    ) {
                        Text(text = "Clear Recent Restaurants")
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Recently Visited")
                }
                RestaurantsLazyList(
                    restaurants = state.recentRestaurants,
                    onRestaurantClick = {
                        val restaurantId = it.documentId
                        navController.navigate(Route.RestaurantScreen.route + "/$restaurantId")
                    }
                )
            }
            else if (state.recentRestaurants.isEmpty() && restaurantName.isEmpty()) {
                //Text(text = "No recent restaurants")
            }
            else if (state.restaurants.isEmpty()) {
                LoadingCircle()
            }else  {
                RestaurantsLazyList(
                    restaurants = state.filteredRestaurantsByNameAndCategories,
                    onRestaurantClick = { restaurant ->
                        val restaurantId = restaurant.documentId
                        navController.navigate(Route.RestaurantScreen.route + "/$restaurantId")
                        viewModel.onEvent(SearchEvent.SaveRecentRestaurantEvent(restaurant))
                        viewModel.onEvent(SearchEvent.SearchedCategoriesEvent(restaurantId))
                    }

                )
            }
        }
    }
}

@Composable
fun RestaurantsLazyList(
    restaurants: List<Restaurant>,
    modifier: Modifier = Modifier,
    onRestaurantClick: (Restaurant) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(restaurants.size) { index ->
            RestaurantCard(
                isNew = false,
                isFavorite = false,
                restaurantId = restaurants[index].documentId,
                name = restaurants[index].name,
                imageUrl = restaurants[index].imageUrl,
                placeName = restaurants[index].placeName,
                averageRating = restaurants[index].averageRating.toFloat(),
                onFavorite = {},
                onClick = { onRestaurantClick(restaurants[index]) },
                showLikeButton = false
            )
            Spacer(modifier = modifier.height(29.dp))
        }
    }
}

@Composable
private fun LoadingCircle(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun SearchScreenPreview() {
//    SearchScreen()
//}