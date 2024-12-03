package com.example.restau.presentation.reviewlist

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.restau.domain.model.User
import com.example.restau.domain.usecases.analyticsUseCases.AnalyticsUseCases
import com.example.restau.domain.usecases.reviewsUseCases.ReviewsUseCases
import com.example.restau.domain.usecases.userUseCases.UserUseCases
import com.example.restau.utils.getConnectivityAsStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class ReviewListViewModel @Inject constructor(
    private val reviewsUseCases: ReviewsUseCases,
    private val userUseCases: UserUseCases,
    private val analyticsUseCases: AnalyticsUseCases,
    private val application: Application
): AndroidViewModel(application) {

    var state by mutableStateOf(ReviewListState())
        private set

    val isConnected: StateFlow<Boolean> = application.getConnectivityAsStateFlow(viewModelScope)

    var currentUser by mutableStateOf(User())
        private set

    var tempRating by  mutableIntStateOf(0)
        private set

    private var startTime by mutableStateOf(Date())

    var isNavigatingCreateReview by  mutableStateOf(false)
        private set

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
            is ReviewListEvent.ScreenOpened -> {
                startTimer()
            }
            is ReviewListEvent.ScreenClosed -> {
                sendEvent()
            }

            is ReviewListEvent.TempReviewRatingChange -> {
                onChangeTempRating(event.tempRating)
            }

            is ReviewListEvent.OnChangeIsNavigatingCreateReview -> {
                onChangeIsNavigatingCreateReview(event.isNavigating)
            }
        }
    }

    private fun getUser() {
        viewModelScope.launch(Dispatchers.IO) {
            currentUser = userUseCases.getUserObject()
        }
    }

    private fun getReviews(restaurantId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("DONITEST", isConnected.value.toString())
            val reviews = reviewsUseCases.getRestaurantsReviews(restaurantId)
            var fallback = false
            if (reviews.isEmpty() && !isConnected.value) {
                fallback = true
            }
            state = state.copy(
                reviews = reviews,
                isLoading = false,
                showFallback = fallback
            )
        }
    }

    private fun onChangeTempRating(newrating: Int) {
        tempRating = newrating
    }

    private fun onChangeIsNavigatingCreateReview(isNavigating: Boolean) {
        isNavigatingCreateReview = isNavigating
    }

}