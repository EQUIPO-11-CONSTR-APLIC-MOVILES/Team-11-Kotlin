package com.example.restau.presentation.random

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.navigation.NavController
import com.example.restau.R
import com.example.restau.presentation.common.LoadingCircle
import com.example.restau.presentation.common.NoConnection
import com.example.restau.presentation.home.HomeEvent
import com.example.restau.presentation.restaurant.RestaurantScreen
import com.example.restau.ui.theme.Poppins

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun RandomScreen(
    navController: NavController,
    randomViewModel: RandomViewModel = hiltViewModel()
) {

    val isConnected by randomViewModel.isConnected.collectAsState()

    LaunchedEffect(isConnected) {
        randomViewModel.onEvent(RandomEvent.ScreenLaunched)
    }

    LaunchedEffect(Unit) {
        if (randomViewModel.restaurantId.isEmpty()) {
            randomViewModel.onEvent(RandomEvent.ScreenLaunched)
        }
    }

    LifecycleResumeEffect(Unit) {
        randomViewModel.onEvent(RandomEvent.ScreenOpened)

        onPauseOrDispose {
            randomViewModel.onEvent(RandomEvent.ScreenClosed)
        }
    }

    if (randomViewModel.showFallback){
        NoConnection()
    }
    else if (randomViewModel.restaurantId.isNotEmpty()) {
        RestaurantScreen(
            restaurantID = randomViewModel.restaurantId,
            navController = navController,
            randomID = randomViewModel.randomID.value
        )
    } else if (randomViewModel.isLoading){
        LoadingCircle()
    } else {
        NoRandomForYou()
    }
}

@Composable
fun NoRandomForYou(
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(horizontal = 8.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.filled_like),
            contentDescription = "Give likes to get random options from your favorites!",
            tint = Color.Gray,
            modifier = Modifier.size(65.dp)
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = "Give likes to get random options from your favorites!",
            color = Color.Gray,
            fontFamily = Poppins,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }
}