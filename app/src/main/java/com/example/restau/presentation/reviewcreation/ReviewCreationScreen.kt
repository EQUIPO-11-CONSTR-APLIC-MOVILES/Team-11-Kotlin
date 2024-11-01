package com.example.restau.presentation.reviewcreation

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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.restau.domain.model.Review
import com.example.restau.presentation.common.DynamicTopBar
import com.example.restau.presentation.common.StarPicker
import com.example.restau.presentation.common.TopBarAction
import com.example.restau.presentation.home.HomeEvent
import com.example.restau.ui.theme.Poppins
import com.example.restau.ui.theme.SoftRed
import com.google.firebase.Timestamp
import java.util.Date

@Composable
fun ReviewCreationScreen(
    navController: NavController,
    restaurantId: String?,
    tempRate: Int,
    restaurantName: String?,
    reviewCreationViewModel: ReviewCreationViewModel = hiltViewModel()
) {

    val state = reviewCreationViewModel.state
    val currentUser = reviewCreationViewModel.currentUser
    val reviewText = reviewCreationViewModel.reviewText
    val showErrorDialog = reviewCreationViewModel.showErrorDialog
    val rating = reviewCreationViewModel.rating

    val isConnected by reviewCreationViewModel.isConnected.collectAsState()

    LaunchedEffect(isConnected) {
        reviewCreationViewModel.onEvent(ReviewCreationEvent.ScreenLaunched)
    }

    LaunchedEffect(Unit) {
        reviewCreationViewModel.onEvent(ReviewCreationEvent.ScreenLaunched)
        reviewCreationViewModel.onEvent(ReviewCreationEvent.ReviewRatingChange(tempRate))
    }

    LaunchedEffect(state.isReviewAddedSuccessfully) {
        if (state.isReviewAddedSuccessfully) {
            navController.popBackStack()
        } else {
            // If fails
        }
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { reviewCreationViewModel.onEvent(ReviewCreationEvent.ShowErrorDialog(false))},
            confirmButton = {
                TextButton(onClick = { reviewCreationViewModel.onEvent(ReviewCreationEvent.ShowErrorDialog(false))
                    if (reviewCreationViewModel.showFallback && reviewText.trim().isNotEmpty()) {
                        navController.popBackStack()
                    }
                },
                    modifier = Modifier
                    .padding(8.dp)
                    .size(width = 120.dp, height = 50.dp)
                    .background(SoftRed, shape = MaterialTheme.shapes.medium)
                ) {
                    Text(
                        "OK",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            },
            title = { Text(text = "Error", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
            text = {ErrorText(rating = rating, reviewText = reviewText, showFallback = reviewCreationViewModel.showFallback)},
            shape = MaterialTheme.shapes.medium
        )
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
                            text = restaurantName ?: "",
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
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(start = 32.dp, end = 32.dp, top = 16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = currentUser.name,
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = Poppins
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = currentUser.profilePic,
                    contentDescription = "User Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(MaterialTheme.shapes.extraLarge)
                        .background(Color.LightGray)
                )
                Spacer(modifier = Modifier.width(20.dp))
                StarPicker(onStarSelect = { selectedRating -> reviewCreationViewModel.onEvent(ReviewCreationEvent.ReviewRatingChange(selectedRating)) },
                value = rating,
                size = 40.dp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = reviewText,
                onValueChange = {
                    if (it.length <= 500) {
                        reviewCreationViewModel.onEvent(ReviewCreationEvent.ReviewTextChange(it))
                    }
                },
                label = { Text(text = "Share details of your own experience at this place") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray,
                    focusedLabelColor = Color.Gray,
                    cursorColor = Color.Gray
                )
            )

            Text(
                text = "${reviewText.length}/500",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp)
                    .wrapContentWidth(Alignment.End)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {

                    val cleanedReviewText = reviewText.trim()

                    if (restaurantId != null && cleanedReviewText.isNotEmpty() && rating != 0) {
                        val review = Review(
                            authorId = currentUser.documentId,
                            authorName = currentUser.name,
                            description = reviewText,
                            rating = rating,
                            restaurantId = restaurantId,
                            authorPFP = currentUser.profilePic,
                            date = Timestamp(Date())
                        )
                        reviewCreationViewModel.onEvent(ReviewCreationEvent.AddReviewEvent(review))
                    } else {
                        reviewCreationViewModel.onEvent(ReviewCreationEvent.ShowErrorDialog(true))
                    }

                    if (reviewCreationViewModel.showFallback){
                        reviewCreationViewModel.onEvent(ReviewCreationEvent.ShowErrorDialog(true))
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SoftRed,
                    contentColor = Color.White
                )
            ) {
                Text("Submit")
            }
        }
    }
}

@Composable
fun ErrorText(rating: Int, reviewText: String, showFallback: Boolean) {
    if (rating == 0) {
        Text("Review rating cannot be 0, please leave a rating", fontSize = 16.sp)
    } else if (reviewText.trim().isEmpty()) {
        Text("Review cannot be empty", fontSize = 16.sp)
    } else if (showFallback) {
        Text("No internet connection detected, but don’t worry your review won’t be lost. It will be posted as soon as the connection is restored.", fontSize = 16.sp)
    }
}

