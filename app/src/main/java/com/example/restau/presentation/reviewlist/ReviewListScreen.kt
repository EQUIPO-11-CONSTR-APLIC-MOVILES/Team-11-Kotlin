package com.example.restau.presentation.reviewlist

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.restau.domain.model.Review
import com.example.restau.domain.model.User
import com.example.restau.presentation.common.DynamicTopBar
import com.example.restau.presentation.common.StarPicker
import com.example.restau.presentation.common.StarRating
import com.example.restau.presentation.common.TopBarAction
import com.example.restau.ui.theme.Poppins
import com.google.firebase.Timestamp
import java.util.Date
import java.util.concurrent.TimeUnit
import kotlin.math.round

@Composable
fun ReviewListScreen(
    navController: NavController,
    restaurantId: String?,
    reviewListViewModel: ReviewListViewModel = hiltViewModel(),
) {

    LaunchedEffect(Unit) {
        reviewListViewModel.onEvent(ReviewListEvent.ScreenLaunched(restaurantId ?: ""))
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
        ReviewListContent(
            modifier = Modifier.padding(
                top = it.calculateTopPadding(),
                start = it.calculateLeftPadding(LayoutDirection.Ltr),
                end = it.calculateRightPadding(LayoutDirection.Rtl),
            ),
            currentUser = reviewListViewModel.currentUser,
            state = reviewListViewModel.state,
            onStarClick = {index ->
                reviewListViewModel.onEvent(ReviewListEvent.StarPressed(index))
            }
        )
    }
}

@Composable
fun ReviewListContent(
    state: ReviewListState,
    currentUser: User,
    onStarClick: (Double) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            item {
                LeaveReview(state.reviewValue, currentUser, onStarClick)
                HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = Color(0xFFB3B3B3))
            }
            items(state.reviews) {
                ReviewCard(review = it)
            }
        }
    }

}

@Composable
private fun LeaveReview(
    value: Double,
    currentUser: User,
    onStarClick: (Double) -> Unit,
    modifier: Modifier = Modifier
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
            StarPicker(onClick = onStarClick, value = value , size = 35.dp)
            
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
            StarRating(value = review.rating.toDouble(), size = 28.dp, showValue = false)
            Spacer(modifier = Modifier.width(8.dp))
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
    HorizontalDivider(modifier = modifier.padding(horizontal = 34.dp).fillMaxWidth())
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
