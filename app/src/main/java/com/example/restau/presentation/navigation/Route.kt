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
    data object SignInScreen: Route(route = "signin", icon = null)
    data object NavigatorScreen: Route(route = "navigator", icon = null)
}

val items = listOf(
    Route.HomeScreen,
    Route.RandomScreen,
    Route.SearchScreen,
    Route.LikeScreen,
    Route.MapScreen,
)

val itemsMap = mapOf(
    Route.HomeScreen.route to 0,
    Route.RandomScreen.route to 1,
    Route.SearchScreen.route to 2,
    Route.LikeScreen.route to 3,
    Route.MapScreen.route to 4,
)