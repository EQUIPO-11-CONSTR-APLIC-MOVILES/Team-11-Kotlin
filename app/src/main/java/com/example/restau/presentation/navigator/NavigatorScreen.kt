package com.example.restau.presentation.navigator

import android.util.Log
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.restau.domain.usecases.AuthUseCases
import com.example.restau.presentation.home.HomeScreen
import com.example.restau.presentation.navigation.NavGraph
import com.example.restau.presentation.navigation.Route
import com.example.restau.presentation.navigation.itemsMap
import com.example.restau.presentation.navigator.components.NavBar
import com.example.restau.presentation.signin.SignInScreen
import com.example.restau.presentation.splashscreen.SplashScreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun NavigatorScreen(
    navigatorViewModel: NavigatorViewModel = hiltViewModel(),
) {

    val navController = rememberNavController()
    val systemUiController = rememberSystemUiController()

    val currentEntry by navController.currentBackStackEntryAsState()

    LaunchedEffect(currentEntry) {
        navigatorViewModel.onEvent(
            NavigatorEvent.SelectedChange(itemsMap[(currentEntry?.destination?.route)?: Route.HomeScreen.route]?: 0)
        )
    }

    LaunchedEffect(Unit) {
        navigatorViewModel.onEvent(
            NavigatorEvent.AuthCheck(AuthUseCases.isSignedIn)
        )
    }

    if (!navigatorViewModel.showSplash) {
        systemUiController.setSystemBarsColor(
            color = Color.Gray
        )
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {

                if (AuthUseCases.isSignedIn) {
                    LoadHomeScreen(navigatorViewModel = navigatorViewModel, navController = navController)
                }
                else{
                    SignInScreen(navController = navController)
                }
            }
        ) {
            NavigatorContent(
                modifier = Modifier.padding(
                    start = it.calculateStartPadding(
                        LayoutDirection.Ltr
                    ),
                    end = it.calculateEndPadding(LayoutDirection.Rtl),
                    bottom = it.calculateBottomPadding()
                ), navHostController = navController
            )
        }
    } else {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent
        )
        SplashScreen()
    }
}

@Composable
fun LoadHomeScreen(navigatorViewModel: NavigatorViewModel, navController: NavHostController) {
    NavBar(
        selected = navigatorViewModel.selected,
        onNav = {
            navController.navigate(it)
        },
        onSelected = {
            navigatorViewModel.onEvent(
                NavigatorEvent.SelectedChange(it)
            )
        }
    )
}

@Composable
private fun NavigatorContent(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxSize()
    ) {
        NavGraph(navHostController = navHostController)
    }
}


