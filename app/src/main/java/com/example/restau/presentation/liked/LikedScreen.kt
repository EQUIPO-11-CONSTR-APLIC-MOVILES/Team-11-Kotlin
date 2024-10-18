package com.example.restau.presentation.liked

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.navigation.NavHostController
import com.example.restau.R
import com.example.restau.presentation.common.DynamicTopBar
import com.example.restau.presentation.common.LoadingCircle
import com.example.restau.presentation.common.RestaurantsLazyList
import com.example.restau.presentation.common.TopBarAction
import com.example.restau.ui.theme.Poppins

@Composable
fun LikedScreen(
    navController: NavHostController,
    likedViewModel: LikedViewModel = hiltViewModel()
) {

    val user = likedViewModel.currentUser

    LaunchedEffect(Unit) {
        likedViewModel.onEvent(LikedEvent.ScreenLaunched)
    }

    LifecycleResumeEffect(Unit) {
        likedViewModel.onEvent(LikedEvent.ScreenOpened)

        onPauseOrDispose {
            likedViewModel.onEvent(LikedEvent.ScreenClosed)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            DynamicTopBar(
                label = {},
                hasBackButton = false,
                action = TopBarAction.PhotoAction(user.profilePic) {}
            )
        }
    ) {
        LikedContent(
            state = likedViewModel.state,
            onLike = {documentId, delete ->
                likedViewModel.onEvent(LikedEvent.SendLike(documentId, delete))
            },
            modifier = Modifier.padding(
                top = it.calculateTopPadding(),
                start = it.calculateStartPadding(LayoutDirection.Ltr),
                end = it.calculateEndPadding(LayoutDirection.Rtl)
            ),
            navController = navController
        )
    }
}

@Composable
fun LikedContent(
    state: LikedState,
    onLike: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    navController: NavHostController
) {

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        if (state.restaurants.isEmpty()) {
            LoadingCircle()
        } else if (state.isLiked.all { !it }) {
            NoLikedRestaurants()
        } else {
            Spacer(modifier = Modifier.height(22.dp))
            RestaurantsLazyList(
                restaurants = state.restaurants,
                isNew = state.isNew,
                isShown = state.isLiked,
                isLiked = state.isLiked,
                onLike = onLike,
                navController = navController
            )
        }
    }
}

@Composable
fun NoLikedRestaurants(
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Icon(
            painter = painterResource(id = R.drawable.filled_like),
            contentDescription = "No Open Restaurants",
            tint = Color.Gray,
            modifier = Modifier.size(65.dp)
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = "No Liked Restaurants",
            color = Color.Gray,
            fontFamily = Poppins,
            fontSize = 25.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }
}