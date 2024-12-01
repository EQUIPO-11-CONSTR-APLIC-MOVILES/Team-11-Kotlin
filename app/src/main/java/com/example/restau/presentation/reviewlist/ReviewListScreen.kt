package com.example.restau.presentation.reviewlist

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.restau.R
import com.example.restau.domain.model.Review
import com.example.restau.domain.model.User
import com.example.restau.presentation.common.DynamicTopBar
import com.example.restau.presentation.common.LoadingCircle
import com.example.restau.presentation.common.NoConnection
import com.example.restau.presentation.common.StarPicker
import com.example.restau.presentation.common.StarRating
import com.example.restau.presentation.common.TopBarAction
import com.example.restau.presentation.navigation.Route
import com.example.restau.ui.theme.Poppins
import com.google.firebase.Timestamp
import java.util.Date
import java.util.concurrent.TimeUnit
import kotlin.math.round

@Composable
fun ReviewListScreen(
    navController: NavController,
    restaurantId: String?,
    restaurantName: String?,
    reviewListViewModel: ReviewListViewModel = hiltViewModel(),
    randomID: String = ""
) {

    val tempRating = reviewListViewModel.tempRating

    val isConnected by reviewListViewModel.isConnected.collectAsState()

    LaunchedEffect(isConnected) {
        reviewListViewModel.onEvent(ReviewListEvent.ScreenLaunched(restaurantId ?: ""))
        reviewListViewModel.onEvent(ReviewListEvent.TempReviewRatingChange(0))
        reviewListViewModel.onEvent(ReviewListEvent.OnChangeIsNavigatingCreateReview(false))
    }

    LifecycleResumeEffect(Unit) {
        reviewListViewModel.onEvent(ReviewListEvent.OnChangeIsNavigatingCreateReview(false))
        reviewListViewModel.onEvent(ReviewListEvent.ScreenOpened)

        onPauseOrDispose {
            reviewListViewModel.onEvent(ReviewListEvent.ScreenClosed)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            DynamicTopBar(
                label = { 
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Reviews",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = Poppins
                        )
                    }
                },
                hasBackButton = true,
                action = TopBarAction.NoAction,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    ) {
        Log.d("DONITEST", reviewListViewModel.state.showFallback.toString())
        ReviewListContent(
            showFallback = reviewListViewModel.state.showFallback,
            modifier = Modifier.padding(
                top = it.calculateTopPadding(),
                start = it.calculateLeftPadding(LayoutDirection.Ltr),
                end = it.calculateRightPadding(LayoutDirection.Rtl),
            ),
            currentUser = reviewListViewModel.currentUser,
            state = reviewListViewModel.state,
            reviewListViewModel = reviewListViewModel,
            navController = navController,
            restaurantId = restaurantId,
            restaurantName = restaurantName,
            tempRating = tempRating,
            randomID = randomID
        )
    }
}

@Composable
fun ReviewListContent(
    showFallback: Boolean,
    state: ReviewListState,
    currentUser: User,
    navController: NavController,
    restaurantId: String?,
    restaurantName: String?,
    reviewListViewModel: ReviewListViewModel,
    tempRating: Int,
    modifier: Modifier = Modifier,
    randomID: String?
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (!showFallback) {
            if (state.reviews.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    item {
                        LeaveReview(
                            currentUser,
                            navController,
                            restaurantId,
                            restaurantName,
                            reviewListViewModel,
                            tempRating,
                            randomID = randomID
                        )
                        HorizontalDivider(
                            modifier = Modifier.fillMaxWidth(),
                            thickness = 1.dp,
                            color = Color(0xFFB3B3B3)
                        )
                    }
                    items(state.reviews) {
                        ReviewCard(review = it)
                    }
                }
            } else if (!state.isLoading) {
                LeaveReview(
                    currentUser,
                    navController,
                    restaurantId,
                    restaurantName,
                    reviewListViewModel,
                    tempRating,
                    randomID = randomID
                )
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = Color(0xFFB3B3B3)
                )
                NoReviews()
            } else {
                LoadingCircle()
            }
        } else {
            NoConnection()
        }
    }
}

@Composable
fun NoReviews(
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Icon(
            painter = painterResource(id = R.drawable.star),
            contentDescription = "Be the first one to review!",
            tint = Color.Gray,
            modifier = Modifier.size(65.dp)
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = "Be the first one to review!",
            color = Color.Gray,
            fontFamily = Poppins,
            fontSize = 25.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun LeaveReview(
    currentUser: User,
    navController: NavController,
    restaurantId: String?,
    restaurantName: String?,
    reviewListViewModel: ReviewListViewModel,
    tempRating: Int,
    modifier: Modifier = Modifier,
    randomID: String?
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 45.dp, top = 25.dp, bottom = 30.dp, end = 45.dp)
    ) {
        Text(
            modifier = modifier.fillMaxWidth(),
            text = "Rate & Review",
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = Poppins
        )
        Spacer(modifier = Modifier.height(3.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = currentUser.profilePic,
                contentDescription = "User Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(35.dp)
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(Color.LightGray)
            )
            Spacer(modifier = Modifier.width(20.dp))
            StarPicker(onStarSelect = { selectedRating ->
                if(!reviewListViewModel.isNavigatingCreateReview) {
                    reviewListViewModel.onEvent(ReviewListEvent.OnChangeIsNavigatingCreateReview(true))
                    reviewListViewModel.onEvent(ReviewListEvent.TempReviewRatingChange(selectedRating))
                    navController.navigate(Route.ReviewCreationScreen.route + "/${restaurantId}" + "/${selectedRating}" + "/${restaurantName}" + "/${randomID}")

                }
            },
            value = tempRating, size =30.dp)
        }
    }
}


@Composable
private fun ReviewCard(
    review: Review,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 45.dp, vertical = 36.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = review.authorPFP,
                contentDescription = "User Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(31.dp)
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(Color.LightGray)
            )
            Spacer(modifier = Modifier.width(18.dp))
            Text(
                text = review.authorName,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = Poppins
            )
        }
        Spacer(modifier = Modifier.height(9.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            StarRating(value = review.rating.toDouble(), size = 22.dp, showValue = false)
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = getWeekString(review.date),
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = Poppins,
                color = Color(0xFFA5A5A5)
            )
        }
        Spacer(modifier = Modifier.height(23.dp))
        Text(
            text = review.description,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = Poppins,
            textAlign = TextAlign.Justify,
            maxLines = 7
        )
    }
    HorizontalDivider(modifier = modifier
        .padding(horizontal = 34.dp)
        .fillMaxWidth())
}


private fun getWeekString(date: Timestamp): String {
    val now = Date().time

    // Get the time from the timestamp
    val past = date.toDate().time

    // Calculate the difference in milliseconds
    val diffInMillis = now - past

    // Convert milliseconds to weeks
    val weeks =  round(TimeUnit.MILLISECONDS.toDays(diffInMillis) / 7.0).toLong()

    return if (weeks <= 1) {
        "this week"
    } else {
        "$weeks weeks ago"
    }
}
