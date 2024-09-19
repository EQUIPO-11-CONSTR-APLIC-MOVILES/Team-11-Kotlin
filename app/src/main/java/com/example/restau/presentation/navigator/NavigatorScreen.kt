package com.example.restau.presentation.navigator

import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.restau.presentation.navigation.NavGraph
import com.example.restau.presentation.navigation.Route
import com.example.restau.presentation.navigation.itemsMap
import com.example.restau.presentation.navigator.components.NavBar
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
            NavigatorEvent.AuthCheck()
        )
    }

    if (!navigatorViewModel.showSplash) {
        systemUiController.setSystemBarsColor(
            color = Color.Gray
        )
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {

                val currentUser by navigatorViewModel.currentUser.collectAsState()

                if (navigatorViewModel.isSignedIn or (currentUser != null)) {
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
            }
        ) {
            NavigatorContent(
                modifier = Modifier.padding(
                    start = it.calculateStartPadding(
                        LayoutDirection.Ltr
                    ),
                    end = it.calculateEndPadding(LayoutDirection.Rtl),
                    bottom = it.calculateBottomPadding()
                ), navHostController = navController,
                isSignedIn = navigatorViewModel.isSignedIn,
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
private fun NavigatorContent(
    navHostController: NavHostController,
    isSignedIn: Boolean,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxSize()
    ) {
        NavGraph(navHostController = navHostController, isSignedIn)
    }
}


