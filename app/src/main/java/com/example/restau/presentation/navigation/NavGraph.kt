package com.example.restau.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.restau.presentation.home.HomeScreen
import com.example.restau.presentation.liked.LikedScreen
import com.example.restau.presentation.map.MapScreen
import com.example.restau.presentation.preferences.PreferencesScreen
import com.example.restau.presentation.random.RandomScreen
import com.example.restau.presentation.restaurant.RestaurantScreen
import com.example.restau.presentation.reviewlist.ReviewListScreen
import com.example.restau.presentation.search.SearchScreen
import com.example.restau.presentation.signin.SignInScreen
import com.example.restau.presentation.signup.SignUpScreen

@Composable
fun NavGraph(
    navHostController: NavHostController,
    isSignedIn: Boolean,
    authCheck: suspend () -> Unit
) {
     NavHost(navController = navHostController, startDestination = if (isSignedIn) Route.HomeScreen.route else Route.SignInScreen.route ) {

        composable(route = Route.HomeScreen.route) {
            HomeScreen(navController = navHostController)
        }
        composable(route = Route.RandomScreen.route) {
            RandomScreen(navController = navHostController)
        }
        composable(route = Route.SearchScreen.route) {
            SearchScreen(navController = navHostController)
        }
        composable(route = Route.LikeScreen.route) {
            LikedScreen(navController = navHostController)
        }
        composable(route = Route.MapScreen.route) {
            MapScreen(navController = navHostController)
        }
        composable(route = Route.SignInScreen.route) {
            SignInScreen(navHostController, authCheck)
        }
         composable(route = Route.SignUpScreen.route) {
             SignUpScreen(navController = navHostController)
         }

         composable(route = Route.PreferencesScreen.route) {
             PreferencesScreen(navController = navHostController, authCheck = authCheck)
         }
         composable(
             route = Route.ReviewListScreen.route + "/{restaurantId}",
             arguments = listOf(navArgument("restaurantId") { type = NavType.StringType })
         ){
            val id = it.arguments?.getString("restaurantId")
            ReviewListScreen(navController = navHostController, restaurantId = id)
         }


         composable(
             route = Route.RestaurantScreen.route + "/{restaurantID}",
             arguments = listOf(navArgument("restaurantID") { type = NavType.StringType })
         ){
             val id = it.arguments?.getString("restaurantID")
             RestaurantScreen(restaurantID = id, navController = navHostController)
         }
    }
}
