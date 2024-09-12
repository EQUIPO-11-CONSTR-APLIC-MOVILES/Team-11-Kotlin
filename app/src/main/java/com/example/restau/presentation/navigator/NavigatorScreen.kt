package com.example.restau.presentation.navigator

import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.restau.presentation.navigation.NavGraph
import com.example.restau.presentation.navigator.components.NavBar
import com.example.restau.presentation.splashscreen.SplashScreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun NavigatorScreen(
    navigatorViewModel: NavigatorViewModel = hiltViewModel(),
) {

    val navController = rememberNavController()
    val systemUiController = rememberSystemUiController()

    if (!navigatorViewModel.showSplash) {
        systemUiController.setSystemBarsColor(
            color = Color.Gray
        )
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                NavBar(
                    selected = navigatorViewModel.selected,
                    onNav = { onNavTab(navController, it) },
                    onSelected = {
                        navigatorViewModel.onEvent(
                            NavigatorEvent.SelectedChange(it)
                        )
                    }
                )
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
fun NavigatorContent(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxSize()
    ) {
        NavGraph(navHostController = navHostController)
    }
}

private fun onNavTab(navController: NavController, route: String) {
    navController.navigate(route) {
        navController.graph.startDestinationRoute?.let { screenRoute ->
            popUpTo(screenRoute) {
                saveState = true
            }
        }
        launchSingleTop = true
        restoreState = true
    }
}
