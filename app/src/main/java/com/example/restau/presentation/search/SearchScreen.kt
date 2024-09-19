package com.example.restau.presentation.search

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.restau.domain.model.Restaurant
import com.example.restau.presentation.common.DynamicTopBar
import com.example.restau.presentation.common.RestaurantCard
import com.example.restau.presentation.common.TopBarAction
import com.google.firebase.Timestamp
import java.util.Date
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
                onValueChange = { viewModel.onRestaurantNameChange(it) },
                label = { Text("Restaurant Name or Categories") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = SoftRed,
                    focusedLabelColor = SoftRed,
                    cursorColor = SoftRed
                ),
                trailingIcon = {
                    IconButton(onClick = {  viewModel.onRestaurantNameChange("") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.cleartext),
                            contentDescription = "Clear text"
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (state.recentRestaurants.isNotEmpty() && restaurantName.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Button(onClick = { viewModel.clearRecentRestaurants() },
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
                    restaurantName = restaurantName,
                    restaurants = state.recentRestaurants,
                    onRestaurantClick = { restaurantId ->
                        viewModel.saveRecentRestaurant(restaurantId)
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
                    restaurants = state.restaurants,
                    restaurantName = restaurantName,
                    onRestaurantClick = { restaurantId ->
                        viewModel.saveRecentRestaurant(restaurantId)
                    }

                )
            }
        }
    }
}

@Composable
fun RestaurantsLazyList(
    restaurants: List<Restaurant>,
    restaurantName: String,
    modifier: Modifier = Modifier,
    onRestaurantClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(restaurants.filter {
            it.name.contains(restaurantName, ignoreCase = true) ||
                    it.categories.any { category ->
                        category.contains(restaurantName, ignoreCase = true)
                    }
        }) { restaurant ->
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
            Spacer(modifier = Modifier.height(29.dp))
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