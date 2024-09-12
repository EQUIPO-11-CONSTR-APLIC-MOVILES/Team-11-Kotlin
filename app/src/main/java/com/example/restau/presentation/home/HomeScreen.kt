package com.example.restau.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.restau.domain.model.Restaurant
import com.example.restau.presentation.common.DynamicTopBar
import com.example.restau.presentation.common.RestaurantCard
import com.example.restau.presentation.common.TopBarAction
import com.example.restau.ui.theme.Poppins

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            DynamicTopBar(
                label = {},
                hasBackButton = false,
                //TODO: Change image to profile when login is created
                action = TopBarAction.PhotoAction("https://media.licdn.com/dms/image/D5603AQEBrjq29ydePA/profile-displayphoto-shrink_200_200/0/1718318242718?e=2147483647&v=beta&t=zjoTlF9eMNeo-jDFcL0iqR58RwZCcWq9t6zdJeljYbw") {}
            )
        }
    ) {
        HomeContent(
            state = homeViewModel.state,
            onFilterClick = {filterIndex ->
                homeViewModel.onEvent(HomeEvent.FilterEvent(filterIndex))
            },
            modifier = Modifier.padding(it)
        )
    }
}

@Composable
fun HomeContent(
    state: HomeState,
    onFilterClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        FilterRow(
            selected = state.selectedFilter,
            onClick = onFilterClick
        )
        RestaurantsLazyList(
            restaurants = state.restaurants
        )
    }
}

@Composable
fun RestaurantsLazyList(
    restaurants: List<Restaurant>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(start = 41.dp, end = 41.dp)
    ) {
        items(restaurants) {restaurant ->
            RestaurantCard(
                //TODO check if it is new
                isNew = true,
                isFavorite = true,
                name = restaurant.name,
                imageUrl = "https://www.allrecipes.com/thmb/lLeKelVvgs-yPAgqDfGrSzOOJIs=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/4431200-6c6df37091d341f3938dd5a4ee4b5f62.jpg",
                placeName = restaurant.placeName,
                averageRating = restaurant.averageRating.toFloat(),
                onFavorite = {}
            )
        }
    }
}

@Composable
fun FilterRow(
    selected: Int,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {

    val items = listOf(
        "Open Now",
        "For you",
        "All"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp)
            .padding(vertical = 25.dp, horizontal = 40.dp)
    ) {
        items.forEachIndexed { index, label ->
            SuggestionChip(
                onClick = { onClick(index) },
                enabled = selected != index,
                label = {
                    Text(
                        text = label,
                        fontFamily = Poppins,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                colors = SuggestionChipDefaults.suggestionChipColors().copy(
                    containerColor = Color.White,
                    labelColor = Color.Black,
                    disabledContainerColor = MaterialTheme.colorScheme.secondary,
                    disabledLabelColor = Color.White
                ),
                modifier = Modifier
                    .width(106.dp)
                    .height(41.dp)
            )
        }
    }
}
