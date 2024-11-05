package com.example.restau.presentation.liked

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restau.domain.model.Restaurant
import com.example.restau.domain.model.User
import com.example.restau.domain.usecases.analyticsUseCases.AnalyticsUseCases
import com.example.restau.domain.usecases.restaurantUseCases.RestaurantUseCases
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
class LikedViewModel @Inject constructor(
    private val analyticsUseCases: AnalyticsUseCases,
    private val userUseCases: UserUseCases,
    private val restaurantUseCases: RestaurantUseCases,
    private val application: Application
): AndroidViewModel(application) {

    val isConnected: StateFlow<Boolean> = application.getConnectivityAsStateFlow(viewModelScope)

    var showFallback by mutableStateOf(false)
        private set

    var reload by mutableStateOf(false)
        private set

    var state by mutableStateOf(LikedState())
        private set

    var currentUser by mutableStateOf(User())
        private set

    private var startTime by mutableStateOf(Date())

    private fun updateUserAndData() {
        viewModelScope.launch(Dispatchers.IO) {
            val user = userUseCases.getUserObject()
            currentUser = user
            val restaurants = restaurantUseCases.getRestaurants()
            if (!isConnected.value && restaurants.isEmpty()) {
                showFallback = true
            } else {
                showFallback = false
                getRestaurants()
            }

        }
    }

    private fun startTimer() {
        startTime = Date()
    }

    private fun sendEvent() {
        viewModelScope.launch(Dispatchers.IO) {
            val miliDifference = Date().time - startTime.time
            val timer = TimeUnit.MILLISECONDS.toSeconds(miliDifference)
            analyticsUseCases.sendScreenTimeEvent("liked_screen", timer, currentUser.documentId)
        }
    }

    fun onEvent(event: LikedEvent) {
        when(event) {
            is LikedEvent.ScreenOpened -> {
                startTimer()
            }
            is LikedEvent.ScreenClosed -> {
                sendEvent()
            }
            is LikedEvent.SendLike -> {
                modifyLike(event.documentId, event.delete)
            }
            is LikedEvent.ScreenLaunched -> {
                updateUserAndData()
                reload = !reload
            }
        }
    }

    private fun modifyLike(documentId: String, delete: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val likes = currentUser.likes.toMutableList()
            if (delete) {
                likes.remove(documentId)
            } else {
                likes.add(documentId)
            }
            currentUser = currentUser.copy(
                likes = likes
            )
            updateRestaurantsState(state.restaurants)
            userUseCases.sendLike(currentUser)
        }
    }

    private fun getRestaurants() {
        viewModelScope.launch(Dispatchers.IO) {
            val restaurants = restaurantUseCases.getRestaurants()
            updateRestaurantsState(restaurants)
        }
    }

    private fun updateRestaurantsState(restaurants: List<Restaurant>) {
        state = state.copy(
            restaurants = restaurants,
            isNew = restaurantUseCases.getIsNewRestaurantArray(restaurants),
            isLiked = restaurantUseCases.getRestaurantsLiked(restaurants, currentUser.likes)
        )
    }


}