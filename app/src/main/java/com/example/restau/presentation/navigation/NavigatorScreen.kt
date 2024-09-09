package com.example.restau.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.restau.presentation.navigation.components.NavBar

@Composable
fun NavigatorScreen(
    navigatorViewModel: NavigatorViewModel = hiltViewModel(),
) {

    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavBar(onNav = {  }, selected = navigatorViewModel.selected)
        }
    ) {
        NavigatorContent(modifier = Modifier.padding(it), navHostController = navController)
    }
}

@Composable
fun NavigatorContent(
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize()
    ) {
        NavGraph(navHostController = navHostController)
    }
}
