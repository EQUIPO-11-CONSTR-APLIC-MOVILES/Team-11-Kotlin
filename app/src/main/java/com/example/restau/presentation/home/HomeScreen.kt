package com.example.restau.presentation.home

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.restau.R
import com.example.restau.domain.model.Restaurant
import com.example.restau.presentation.common.DynamicTopBar
import com.example.restau.presentation.common.LoadingCircle
import com.example.restau.presentation.common.RestaurantCard
import com.example.restau.presentation.common.TopBarAction
import com.example.restau.ui.theme.Poppins

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val user = homeViewModel.currentUser
    Log.d("DONITEST", user.email + " SCREEN")
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            DynamicTopBar(
                label = {},
                hasBackButton = false,
                action = TopBarAction.PhotoAction(user.profilePic) {}
            )
        }
    ) {
        HomeContent(
            state = homeViewModel.state,
            onFilterClick = { filterIndex ->
                homeViewModel.onEvent(HomeEvent.FilterEvent(filterIndex))
            },
            onLike = {documentId, delete ->
                homeViewModel.onEvent(HomeEvent.SendLike(documentId, delete))
            },
            modifier = Modifier.padding(
                top = it.calculateTopPadding(),
                start = it.calculateStartPadding(LayoutDirection.Ltr),
                end = it.calculateEndPadding(LayoutDirection.Rtl)
            )
        )
    }
}

@Composable
fun HomeContent(
    state: HomeState,
    onFilterClick: (Int) -> Unit,
    onLike: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        FilterRow(
            selected = state.selectedFilter,
            onClick = onFilterClick
        )
        if (state.restaurants.isEmpty() && state.nothingOpen) {
            NoRestaurantOpen()
        } else if (state.restaurants.isEmpty()) {
            LoadingCircle()
        } else {
            RestaurantsLazyList(
                restaurants = state.restaurants,
                isNew = state.isNew,
                isLiked = state.isLiked,
                onLike = onLike
            )
        }
    }
}

@Composable
fun NoRestaurantOpen(
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Icon(
            painter = painterResource(id = R.drawable.clock),
            contentDescription = "No Open Restaurants",
            tint = Color.Gray,
            modifier = Modifier.size(65.dp)
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = "No Open Restaurants",
            color = Color.Gray,
            fontFamily = Poppins,
            fontSize = 25.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun RestaurantsLazyList(
    restaurants: List<Restaurant>,
    isNew: List<Boolean>,
    isLiked: List<Boolean>,
    onLike: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 25.dp)
    ) {
        items(restaurants.size) { index ->
            RestaurantCard(
                isNew = isNew[index],
                isFavorite = isLiked[index],
                name = restaurants[index].name,
                imageUrl = restaurants[index].imageUrl,
                placeName = restaurants[index].placeName,
                averageRating = restaurants[index].averageRating.toFloat(),
                onFavorite = { onLike(restaurants[index].documentId, isLiked[index]) },
                onClick = {}
            )
            Spacer(modifier = Modifier.height(29.dp))
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
            .padding(vertical = 25.dp, horizontal = 25.dp)
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
