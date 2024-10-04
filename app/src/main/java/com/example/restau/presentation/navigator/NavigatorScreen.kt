package com.example.restau.presentation.navigator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.restau.presentation.navigation.NavGraph
import com.example.restau.presentation.navigation.Route
import com.example.restau.presentation.navigation.itemsMap
import com.example.restau.presentation.navigator.components.NavBar
import com.example.restau.presentation.splashscreen.SplashScreen
import com.example.restau.ui.theme.Poppins
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun NavigatorScreen(
    navigatorViewModel: NavigatorViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()
    val systemUiController = rememberSystemUiController()

    val currentEntry by navController.currentBackStackEntryAsState()

    val currentUser by navigatorViewModel.currentUser.collectAsState()

    val context = LocalContext.current

    val alertMessage by navigatorViewModel.alertMessage.collectAsState()


    LaunchedEffect(currentEntry) {
        navigatorViewModel.onEvent(
            NavigatorEvent.SelectedChange(itemsMap[(currentEntry?.destination?.route)?: Route.HomeScreen.route]?: 0)
        )
    }

    LaunchedEffect(Unit) {
        navigatorViewModel.onEvent(
            NavigatorEvent.OnStart(context)
        )
    }



    if (!navigatorViewModel.showSplash) {
        systemUiController.setSystemBarsColor(
            color = Color.Gray
        )
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                if (currentUser != null) {
                    NavBar(
                        selected = navigatorViewModel.selected,
                        onNav = {
                            navController.navigate(it) {
                                launchSingleTop = true
                            }
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
            if (alertMessage != null) {
                AlertDialog(
                    modifier = Modifier
                        .padding(16.dp)
                        .background(Color.White, shape = RoundedCornerShape(10.dp)),
                    containerColor = Color.White,
                    onDismissRequest = {
                        navigatorViewModel.alertMessage.value = null
                    },
                    text = {
                        Text(
                            text = alertMessage!!,
                            fontFamily = Poppins,
                            color = Color.DarkGray,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                navigatorViewModel.alertMessage.value = null
                                navController.navigate(Route.MapScreen.route)
                            }
                        ) {
                            Text(
                                text = "Let's go!",
                                fontFamily = Poppins,
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                navigatorViewModel.alertMessage.value = null
                            }
                        ) {
                            Text(
                                text = "Dismiss",
                                fontFamily = Poppins,
                                color = Color.Gray,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }

                )
            }


            NavigatorContent(
                modifier = Modifier.padding(
                    start = it.calculateStartPadding(
                        LayoutDirection.Ltr
                    ),
                    end = it.calculateEndPadding(LayoutDirection.Rtl),
                    bottom = it.calculateBottomPadding()
                ), navHostController = navController,
                isSignedIn = currentUser != null,
                navigatorViewModel = navigatorViewModel
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
    navigatorViewModel: NavigatorViewModel
) {
    Surface(
        modifier = modifier.fillMaxSize()
    ) {
        NavGraph(navHostController = navHostController, isSignedIn, suspend { navigatorViewModel.authCheck() } )
    }
}


