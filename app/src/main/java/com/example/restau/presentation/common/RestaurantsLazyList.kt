package com.example.restau.presentation.common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.restau.domain.model.Restaurant

@Composable
fun RestaurantsLazyList(
    restaurants: List<Restaurant>,
    isNew: List<Boolean>,
    isLiked: List<Boolean>,
    onLike: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    isShown: List<Boolean> = restaurants.map { true },
    onClick: (String) -> Unit,
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 25.dp)
    ) {
        items(restaurants.size) { index ->
            if (isShown[index]) {
                RestaurantCard(
                    isNew = isNew[index],
                    isFavorite = isLiked[index],
                    name = restaurants[index].name,
                    imageUrl = restaurants[index].imageUrl,
                    placeName = restaurants[index].placeName,
                    averageRating = restaurants[index].averageRating.toFloat(),
                    onFavorite = { onLike(restaurants[index].documentId, isLiked[index]) },
                    onClick = {
                        onClick("/${restaurants[index].documentId}")
                    }
                )
                Spacer(modifier = Modifier.height(29.dp))
            }
        }
    }
}

