package com.example.restau.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.restau.presentation.home.HomeScreen
import com.example.restau.presentation.liked.LikedScreen
import com.example.restau.presentation.map.MapScreen
import com.example.restau.presentation.random.RandomScreen
import com.example.restau.presentation.search.SearchScreen

@Composable
fun NavGraph(navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = Route.HomeScreen.route) {

        composable(route = Route.HomeScreen.route) {
            HomeScreen()
        }
        composable(route = Route.RandomScreen.route) {
            RandomScreen()
        }
        composable(route = Route.SearchScreen.route) {
            SearchScreen()
        }
        composable(route = Route.LikeScreen.route) {
            LikedScreen()
        }
        composable(route = Route.MapScreen.route) {
            MapScreen()
        }
    }
}