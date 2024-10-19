package com.example.restau.presentation.random

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restau.domain.model.User
import com.example.restau.domain.usecases.analyticsUseCases.AnalyticsUseCases
import com.example.restau.domain.usecases.restaurantUseCases.RestaurantUseCases
import com.example.restau.domain.usecases.userUseCases.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class RandomViewModel @Inject constructor(
    private val restaurantUseCases: RestaurantUseCases,
    private val userUseCases: UserUseCases,
    private val analyticsUseCases: AnalyticsUseCases
): ViewModel() {

    var restaurantId by mutableStateOf("")
        private set

    var isLoading by mutableStateOf(true)
        private set

    var currentUser by mutableStateOf(User())
        private set

    private var startTime by mutableStateOf(Date())


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
            val restaurantIds =
                restaurantUseCases
                .getRestaurants()
                .map { it.documentId }
                .filter { it in currentUser.likes }
            if (restaurantIds.isNotEmpty()) {
                restaurantId = restaurantIds.random()
            }
            isLoading = false
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