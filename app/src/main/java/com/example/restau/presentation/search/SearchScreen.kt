package com.example.restau.presentation.search

import android.app.Activity
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.restau.domain.model.Restaurant
import com.example.restau.presentation.common.DynamicTopBar
import com.example.restau.presentation.common.RestaurantCard
import com.example.restau.presentation.common.TopBarAction
import com.example.restau.ui.theme.SoftRed
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.restau.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val restaurantName = viewModel.restaurantName

    val context = LocalContext.current
    val activity = context as? Activity

    val speechRecognizerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val spokenText = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0)
        if (!spokenText.isNullOrEmpty()) {
            viewModel.onEvent(SearchEvent.VoiceRecognitionChangeEvent(spokenText))
        }
    }



    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            DynamicTopBar(
                label = { Text("Search Restaurants") },
                hasBackButton = true,
                action = TopBarAction.PhotoAction("https://media.licdn.com/dms/image/D5603AQEBrjq29ydePA/profile-displayphoto-shrink_200_200/0/1718318242718?e=2147483647&v=beta&t=zjoTlF9eMNeo-jDFcL0iqR58RwZCcWq9t6zdJeljYbw") {}
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(start = 16.dp, end = 16.dp, top = 8.dp)
                .fillMaxSize()
        ) {
            // Campo de búsqueda
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
                    Row{
                        if (restaurantName.isNotEmpty()){
                            IconButton(onClick = {  viewModel.onEvent(SearchEvent.ChangeNameEvent("")) }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.cleartext),
                                    contentDescription = "Clear text"
                                )
                            }
                        }

                        IconButton(onClick = { viewModel.onEvent(SearchEvent.VoiceRecognitionEvent(activity!!, speechRecognizerLauncher)) }) {
                            Icon(
                                painter = painterResource(id = R.drawable.microphone),
                                contentDescription = "Speak restaurant"
                            )
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (state.recentRestaurants.isNotEmpty() && restaurantName.isEmpty()) {
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
                    onRestaurantClick = { //TODO: Restaurant Detail
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
                    onRestaurantClick = { restaurantId ->
                        viewModel.onEvent(SearchEvent.SaveRecentRestaurantEvent(restaurantId))
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
    onRestaurantClick: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(restaurants) { restaurant ->
            RestaurantCard(
                isNew = true,
                isFavorite = true,
                name = restaurant.name,
                imageUrl = restaurant.imageUrl,
                placeName = restaurant.placeName,
                averageRating = restaurant.averageRating.toFloat(),
                onFavorite = {},
                onClick = { onRestaurantClick(restaurant.documentId) }
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

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    SearchScreen()
}