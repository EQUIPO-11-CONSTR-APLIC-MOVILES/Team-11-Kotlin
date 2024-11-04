package com.example.restau.presentation.random

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.restau.domain.model.Restaurant
import com.example.restau.domain.model.User
import com.example.restau.domain.usecases.analyticsUseCases.AnalyticsUseCases
import com.example.restau.domain.usecases.randomReviewUseCases.RandomReviewUseCases
import com.example.restau.domain.usecases.restaurantUseCases.RestaurantUseCases
import com.example.restau.domain.usecases.userUseCases.UserUseCases
import com.example.restau.utils.getConnectivityAsStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class RandomViewModel @Inject constructor(
    private val restaurantUseCases: RestaurantUseCases,
    private val userUseCases: UserUseCases,
    private val analyticsUseCases: AnalyticsUseCases,
    private val randomReviewUseCases: RandomReviewUseCases,
    private val application: Application
): AndroidViewModel(application) {

    val isConnected: StateFlow<Boolean> = application.getConnectivityAsStateFlow(viewModelScope)
    
    var restaurantId by mutableStateOf("")
        private set

    var isLoading by mutableStateOf(true)
        private set

    var currentUser by mutableStateOf(User())
        private set

    private var startTime by mutableStateOf(Date())

    var showFallback by mutableStateOf(false)
        private set

    var randomID = MutableStateFlow<String?>("")

    fun onEvent(event: RandomEvent){
        when (event) {
            is RandomEvent.ScreenOpened -> {
                startTimer()
            }
            is RandomEvent.ScreenClosed -> {
                sendEvent()
            }
            is RandomEvent.ScreenLaunched -> {
                updateUser()
            }
        }
    }

    private fun startTimer() {
        startTime = Date()
    }

    private fun updateUser() {
        viewModelScope.launch(Dispatchers.IO) {
            val user = userUseCases.getUserObject()
            currentUser = user
            val restaurants = restaurantUseCases.getRestaurants()
            if (!isConnected.value && restaurants.isEmpty()) {
                isLoading = false
                showFallback = true
            } else {
                showFallback = false
                restaurantId = getRandomRestaurant(restaurants)
                if (restaurantId.isNotEmpty() && isConnected.value) {
                    viewModelScope.launch(Dispatchers.IO) {
                        randomID.value = randomReviewUseCases.addRandomReview()
                    }
                }
            }
            isLoading = false
        }
    }

    private fun getRandomRestaurant(restaurants: List<Restaurant>): String {
        val likedRestaurants = restaurants
            .map { it.documentId }
            .filter { it in currentUser.likes }

        return if (likedRestaurants.isNotEmpty()) {
            likedRestaurants.random()
        } else {
            ""
        }
    }


    private fun sendEvent() {
        viewModelScope.launch(Dispatchers.IO) {
            val miliDifference = Date().time - startTime.time
            val timer = TimeUnit.MILLISECONDS.toSeconds(miliDifference)
            analyticsUseCases.sendScreenTimeEvent("random_screen", timer, currentUser.documentId)
        }
    }
}