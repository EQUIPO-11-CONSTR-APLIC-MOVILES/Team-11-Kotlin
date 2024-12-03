package com.example.restau.presentation.restaurant

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restau.domain.model.User
import com.example.restau.domain.usecases.analyticsUseCases.AnalyticsUseCases
import com.example.restau.domain.usecases.locationUseCases.LocationUseCases
import com.example.restau.domain.usecases.restaurantUseCases.RestaurantUseCases
import com.example.restau.domain.usecases.userUseCases.UserUseCases
import com.example.restau.utils.getConnectivityAsStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

@HiltViewModel
class RestaurantViewModel @Inject constructor(
    private val restaurantUseCases: RestaurantUseCases,
    private val userUseCases: UserUseCases,
    private val locationUseCases: LocationUseCases,
    private val analyticsUseCases: AnalyticsUseCases,
    application: Application
): ViewModel(){

    val isConnected: StateFlow<Boolean> = application.getConnectivityAsStateFlow(viewModelScope)

    var state by mutableStateOf(RestaurantState())

    var currentUser by mutableStateOf(User())
        private set

    private var startTime by mutableStateOf(Date())

    var showFallback by mutableStateOf(false)
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
            is RestaurantEvent.LaunchMaps -> {
                launchMaps(event.lat, event.lon, event.name, event.place, event.context)
            }
            is RestaurantEvent.ScreenOpened -> {
                startTimer()
            }
            is RestaurantEvent.ScreenClosed -> {
                sendEvent()
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
            analyticsUseCases.sendScreenTimeEvent("detail_screen", timer, currentUser.documentId)
        }
    }

    private fun onLaunch(restaurantID: String){
        viewModelScope.launch {
            try {
                val user = userUseCases.getUserObject()
                currentUser = user

                val restaurant = restaurantUseCases.getRestaurant(restaurantID)
                state = state.copy(restaurant = restaurant)

                showFallback = !isConnected.value && restaurant.documentId.isEmpty()

                val match = analyticsUseCases.getMatchPercentage(currentUser.documentId, restaurantID)
                state = state.copy(match = match)

                val dist = calculateDistance(state.restaurant.latitude, state.restaurant.longitude)
                state = state.copy(distance = dist)

            } catch (e: Exception) {
                Log.e("RestaurantViewModel", "Error during onLaunch", e)
            }
        }
    }

    private suspend fun calculateDistance(restLat: Double, restLon: Double): Double? {
        return try {
            val degToRad = { degrees: Double -> degrees * Math.PI / 180 }

            val location = locationUseCases.getLocation.invoke().first()
            val lat = location?.latitude ?: return null
            val lon = location.longitude

            val earthRadius = 6371.0 // Earth radius in km
            val dLat = degToRad(restLat - lat)
            val dLon = degToRad(restLon - lon)

            val a = sin(dLat / 2).pow(2) +
                    cos(degToRad(lat)) *
                    cos(degToRad(restLat)) *
                    sin(dLon / 2).pow(2)

            val c = 2 * atan2(sqrt(a), sqrt(1 - a))
            (earthRadius * c).let { "%.2f".format(it).toDouble() }
        } catch (e: Exception) {
            Log.e("RestaurantViewModel", "Error during calculateDistance", e)
            null
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
        }
    }

    private fun showSchedule(){
        viewModelScope.launch {
            val isOpen = restaurantUseCases.isOpen(state.restaurant.documentId)
            state = state.copy(showSchedule = !state.showSchedule, isOpen = isOpen)
        }
    }

    private fun launchMaps(lat: Double, lon: Double, name: String, place: String, context: Context){
        viewModelScope.launch(Dispatchers.IO) {
            analyticsUseCases.sendMapSearchTimes()
        }
        try{
            val intent = locationUseCases.launchMaps(lat, lon, name, place)
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            }
        } catch (e: Exception){
            Log.e("RestaurantViewModel", "Error during launchMaps", e)
        }
    }
}