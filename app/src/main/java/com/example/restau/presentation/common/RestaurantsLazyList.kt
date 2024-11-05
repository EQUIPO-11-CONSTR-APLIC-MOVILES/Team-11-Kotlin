package com.example.restau.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.restau.domain.model.Restaurant
import com.example.restau.ui.theme.Poppins

@Composable
fun RestaurantsLazyList(
    restaurants: List<Restaurant>,
    isNew: List<Boolean>,
    isLiked: List<Boolean>,
    onLike: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    isShown: List<Boolean> = restaurants.map { true },
    onClick: (String) -> Unit,
    reload: Boolean = false,
    isFeatured: List<Boolean> = restaurants.map { false },
    completionPercent: Int = 0,
    showCompletion: Boolean = false
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
                    restaurantId = restaurants[index].documentId,
                    isFavorite = isLiked[index],
                    name = restaurants[index].name,
                    imageUrl = restaurants[index].imageUrl,
                    placeName = restaurants[index].placeName,
                    averageRating = restaurants[index].averageRating.toFloat(),
                    onFavorite = { onLike(restaurants[index].documentId, isLiked[index]) },
                    onClick = {
                        onClick("/${restaurants[index].documentId}")
                    },
                    reload = reload,
                    isFeatured = isFeatured[index]
                )
                Spacer(modifier = Modifier.height(29.dp))
            }
        }
        if (showCompletion) {
            item {
                CompletionTag(completionPercent)
                Spacer(modifier = Modifier.height(29.dp))
            }
        }
    }
}

@Composable
fun CompletionTag(
    value: Int,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.Default.Lightbulb,
            contentDescription = "Completion Icon",
            tint = Color.Gray,
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "You've reviewed ${value}% of all restaurants!",
            color = Color.Gray,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = Poppins,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(0.65f)
        )


    }
}

