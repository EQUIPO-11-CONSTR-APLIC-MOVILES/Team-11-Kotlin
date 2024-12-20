package com.example.restau.presentation.reviewcreation

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.restau.domain.model.Review
import com.example.restau.domain.model.User
import com.example.restau.domain.usecases.analyticsUseCases.AnalyticsUseCases
import com.example.restau.domain.usecases.randomReviewUseCases.RandomReviewUseCases
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
class ReviewCreationViewModel @Inject constructor(
    private val reviewsUseCases: ReviewsUseCases,
    private val userUseCases: UserUseCases,
    private val analyticsUseCases: AnalyticsUseCases,
    private val randomReviewUseCases: RandomReviewUseCases,
    private val application: Application,
): AndroidViewModel(application) {

    val isConnected: StateFlow<Boolean> = application.getConnectivityAsStateFlow(viewModelScope)

    var state by mutableStateOf(ReviewCreationState())
        private set

    var currentUser by mutableStateOf(User())
        private set

    private var startTime by mutableStateOf(Date())

    var reviewText by  mutableStateOf("")
        private set

    var rating by  mutableIntStateOf(0)
        private set

    var showErrorDialog by mutableStateOf(false)
        private set

    var showFallback by mutableStateOf(false)
        private set

    init {
        getUser()
    }

    private fun startTimer() {
        startTime = Date()
    }

    private fun sendEvent() {
        viewModelScope.launch(Dispatchers.IO) {
            val miliDifference = Date().time - startTime.time
            val timer = TimeUnit.MILLISECONDS.toSeconds(miliDifference)
            analyticsUseCases.sendScreenTimeEvent("review_creation_screen", timer, currentUser.documentId)
        }
    }

    private fun onReviewTextChange(newReviewText: String) {
        reviewText = newReviewText
    }

    fun onEvent(event: ReviewCreationEvent) {
        when (event) {
            is ReviewCreationEvent.AddReviewEvent -> {
                addReview(event.review)
                viewModelScope.launch(Dispatchers.IO) {
                    randomReviewUseCases.updateRandomReview(event.randomID!!)
                }
            }

            ReviewCreationEvent.ScreenLaunched -> {
                showFallback = !isConnected.value
            }

            is ReviewCreationEvent.ReviewTextChange -> {
                onReviewTextChange(event.reviewText)
            }

            is ReviewCreationEvent.ShowErrorDialog -> {
                onShowDialogChange(event.show)
            }

            is ReviewCreationEvent.ReviewRatingChange -> {
                onChangeRating(event.reviewRating)
            }
        }
    }

    private fun onShowDialogChange(show: Boolean) {
        showErrorDialog = show
    }

    private fun onChangeRating(newrating: Int) {
        rating = newrating
    }

    private fun getUser() {
        viewModelScope.launch(Dispatchers.IO) {
            currentUser = userUseCases.getUserObject()
        }
    }

    private fun addReview(review: Review) {
        viewModelScope.launch(Dispatchers.IO) {
            state = state.copy(isLoading = true)

            showFallback = !isConnected.value

            val result = reviewsUseCases.addReview(review)

            state = if (result) {
                state.copy(
                    isReviewAddedSuccessfully = true
                )
            } else {
                state.copy(
                    isReviewAddedSuccessfully = false
                )
            }
        }
    }

}