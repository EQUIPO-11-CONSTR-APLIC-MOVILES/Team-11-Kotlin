package com.example.restau.presentation.reviewlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restau.domain.model.User
import com.example.restau.domain.usecases.analyticsUseCases.AnalyticsUseCases
import com.example.restau.domain.usecases.reviewsUseCases.ReviewsUseCases
import com.example.restau.domain.usecases.userUseCases.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class ReviewListViewModel @Inject constructor(
    private val reviewsUseCases: ReviewsUseCases,
    private val userUseCases: UserUseCases,
    private val analyticsUseCases: AnalyticsUseCases
): ViewModel() {

    var state by mutableStateOf(ReviewListState())
        private set

    var currentUser by mutableStateOf(User())
        private set

    private var startTime by mutableStateOf(Date())

    private fun startTimer() {
        startTime = Date()
    }

    private fun sendEvent() {
        viewModelScope.launch(Dispatchers.IO) {
            val miliDifference = Date().time - startTime.time
            val timer = TimeUnit.MILLISECONDS.toSeconds(miliDifference)
            analyticsUseCases.sendScreenTimeEvent("review_list_screen", timer, currentUser.documentId)
        }
    }

    fun onEvent(event: ReviewListEvent) {
        when (event) {
            is ReviewListEvent.ScreenLaunched -> {
                getUser()
                getReviews(event.restaurantId)
            }
            is ReviewListEvent.StarPressed -> {
                changeValue(event.value)
            }
            is ReviewListEvent.ScreenOpened -> {
                //startTimer()
            }
            is ReviewListEvent.ScreenClosed -> {
                //sendEvent()
            }
        }
    }

    private fun changeValue(value: Double) {
        state = state.copy(
            reviewValue = value
        )
    }

    private fun getUser() {
        viewModelScope.launch(Dispatchers.IO) {
            currentUser = userUseCases.getUserObject()
        }
    }

    private fun getReviews(restaurantId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            state = state.copy(
                reviews = reviewsUseCases.getRestaurantsReviews(restaurantId),
                isLoading = false
            )
        }
    }

}