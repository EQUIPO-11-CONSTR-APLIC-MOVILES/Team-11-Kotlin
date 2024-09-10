package com.example.restau.presentation.navigation

import androidx.annotation.DrawableRes
import com.example.restau.R

sealed class Route(
    @DrawableRes val icon: Int?,
    val route: String
) {
    data object HomeScreen: Route(route = "home", icon = R.drawable.restaurant)
    data object RandomScreen: Route(route = "random", icon = R.drawable.random)
    data object SearchScreen: Route(route = "search", icon = R.drawable.search)
    data object LikeScreen: Route(route = "like", icon = R.drawable.like)
    data object MapScreen: Route(route = "map", icon = R.drawable.map)
}

val items = listOf(
    Route.HomeScreen,
    Route.RandomScreen,
    Route.SearchScreen,
    Route.LikeScreen,
    Route.MapScreen,
)