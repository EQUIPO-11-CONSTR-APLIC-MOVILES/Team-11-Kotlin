package com.example.restau.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
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
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.navigation.NavHostController
import com.example.restau.R
import com.example.restau.presentation.common.DynamicTopBar
import com.example.restau.presentation.common.LoadingCircle
import com.example.restau.presentation.common.NoConnection
import com.example.restau.presentation.common.RestaurantsLazyList
import com.example.restau.presentation.common.TopBarAction
import com.example.restau.presentation.navigation.Route
import com.example.restau.ui.theme.Poppins

@Composable
fun HomeScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val user = homeViewModel.currentUser
    val isConnected by homeViewModel.isConnected.collectAsState()

    LaunchedEffect(isConnected) {
        homeViewModel.onEvent(HomeEvent.ScreenLaunched)
    }

    LifecycleResumeEffect(Unit) {
        homeViewModel.onEvent(HomeEvent.ScreenOpened)

        onPauseOrDispose {
            homeViewModel.onEvent(HomeEvent.ScreenClosed)
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
        HomeContent(
            showFallback = homeViewModel.showFallback,
            state = homeViewModel.state,
            onFilterClick = { filterIndex ->
                homeViewModel.onEvent(HomeEvent.FilterEvent(filterIndex))
                if (filterIndex == 0){ //This means that the user select and is on the open now screen (index 0)
                    homeViewModel.onEvent(HomeEvent.FeatureInteraction("open_now_feature"))
                }
                else if (filterIndex == 1){ //This means that the user slect and is on the for you screen (index 1)
                    homeViewModel.onEvent(HomeEvent.FeatureInteraction("for_you_feature"))
                }
            },
            onLike = {documentId, delete ->
                homeViewModel.onEvent(HomeEvent.SendLike(documentId, delete))
                if (!delete) homeViewModel.onEvent(HomeEvent.LikeDateEvent(documentId))
            },
            modifier = Modifier.padding(
                top = it.calculateTopPadding(),
                start = it.calculateStartPadding(LayoutDirection.Ltr),
                end = it.calculateEndPadding(LayoutDirection.Rtl)
            ),
            navController = navController,
            reload = homeViewModel.reload,
            completionPercent = homeViewModel.completion,
            isConnected = isConnected
        )
    }
}

@Composable
fun HomeContent(
    state: HomeState,
    completionPercent: Int,
    onFilterClick: (Int) -> Unit,
    onLike: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    navController: NavHostController,
    showFallback: Boolean,
    isConnected: Boolean,
    reload: Boolean
) {

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        if (!showFallback) {
            FilterRow(
                selected = state.selectedFilter,
                onClick = onFilterClick
            )
            if (state.restaurants.isEmpty() && state.nothingOpen) {
                NoRestaurantOpen()
            } else if (state.restaurants.isEmpty() && state.nothingforyou) {
                NoRestaurantForYou()
            } else if (state.restaurants.isEmpty()) {
                LoadingCircle()
            } else {
                RestaurantsLazyList(
                    restaurants = state.restaurants,
                    isNew = state.isNew,
                    isLiked = state.isLiked,
                    onLike = onLike,
                    onClick = { navController.navigate(Route.RestaurantScreen.route + it) },
                    reload = reload,
                    isFeatured = state.isFeatured,
                    showCompletion = isConnected,
                    completionPercent = completionPercent
                )
            }
        } else {
            NoConnection()
        }
    }
}

@Composable
fun FilterRow(
    selected: Int,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {

    val items = listOf(
        "Open Now",
        "For you",
        "All"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp)
            .padding(vertical = 25.dp, horizontal = 25.dp)
    ) {
        items.forEachIndexed { index, label ->
            SuggestionChip(
                onClick = { onClick(index) },
                enabled = selected != index,
                label = {
                    Text(
                        text = label,
                        fontFamily = Poppins,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                colors = SuggestionChipDefaults.suggestionChipColors().copy(
                    containerColor = Color.White,
                    labelColor = Color.Black,
                    disabledContainerColor = MaterialTheme.colorScheme.secondary,
                    disabledLabelColor = Color.White
                ),
                modifier = Modifier
                    .width(106.dp)
                    .height(41.dp)
            )
        }
    }
}

@Composable
fun NoRestaurantOpen(
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Icon(
            painter = painterResource(id = R.drawable.clock),
            contentDescription = "No Open Restaurants",
            tint = Color.Gray,
            modifier = Modifier.size(65.dp)
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = "No Open Restaurants",
            color = Color.Gray,
            fontFamily = Poppins,
            fontSize = 25.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun NoRestaurantForYou(
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Icon(
            painter = painterResource(id = R.drawable.nfy),
            contentDescription = "No Restaurants For You",
            tint = Color.Gray,
            modifier = Modifier.size(65.dp)
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = "No Restaurants For You",
            color = Color.Gray,
            fontFamily = Poppins,
            fontSize = 25.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }
}
