package com.example.restau.presentation.restaurant

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.restau.domain.model.User
import com.example.restau.domain.usecases.restaurantUseCases.RestaurantUseCases
import com.example.restau.domain.usecases.userUseCases.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestaurantViewModel @Inject constructor(
    private val restaurantUseCases: RestaurantUseCases,
    private val userUseCases: UserUseCases
): ViewModel(){

    var state by mutableStateOf(RestaurantState())

    var currentUser by mutableStateOf(User())
        private set


    fun onEvent(event: RestaurantEvent) {
        when (event) {
            is RestaurantEvent.ScreenLaunched -> {
                onLaunch(event.restaurantID)
            }
            is RestaurantEvent.SendLike -> {
                sendLike(event.restaurantID)
            }
            is RestaurantEvent.ShowSchedule -> {
                showSchedule()
            }
        }
    }


    private fun onLaunch(restaurantID: String){
        viewModelScope.launch {
            try {
                val user = userUseCases.getUserObject()
                currentUser = user

                val restaurant = restaurantUseCases.getRestaurant(restaurantID)
                state = state.copy(restaurant = restaurant)
            } catch (e: Exception) {
                Log.e("RestaurantViewModel", "Error during onLaunch", e)
            }
        }
    }

    private fun sendLike(restaurantID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val likes = currentUser.likes.toMutableList()
            if (likes.contains(restaurantID)) {
                likes.remove(restaurantID)
            } else {
                likes.add(restaurantID)
            }
            currentUser = currentUser.copy(
                likes = likes
            )
            userUseCases.sendLike(currentUser)
            Log.d("RestaurantViewModel", "Like sent: $restaurantID - ${currentUser.likes.toString()}")
        }
    }

    private fun showSchedule(){
        viewModelScope.launch {
            val isOpen = restaurantUseCases.isOpen(state.restaurant.documentId)
            state = state.copy(showSchedule = !state.showSchedule, isOpen = isOpen)
        }
    }
}