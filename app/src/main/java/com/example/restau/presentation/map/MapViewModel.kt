package com.example.restau.presentation.map

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restau.domain.model.Restaurant
import com.example.restau.domain.model.User
import com.example.restau.domain.usecases.analyticsUseCases.AnalyticsUseCases
import com.example.restau.domain.usecases.imagesUseCases.ImageDownloadUseCases
import com.example.restau.domain.usecases.locationUseCases.LocationUseCases
import com.example.restau.domain.usecases.restaurantUseCases.RestaurantUseCases
import com.example.restau.domain.usecases.userUseCases.UserUseCases
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val locationUseCases: LocationUseCases,
    private val restaurantsUseCases: RestaurantUseCases,
    private val imageDownloadUseCases: ImageDownloadUseCases,
    private val userUseCases: UserUseCases,
    private val analyticsUseCases: AnalyticsUseCases
) : ViewModel() {

    var state by mutableStateOf(MapState())
        private set

    var currentLocation by mutableStateOf(LatLng(4.603096177609384, -74.06584744436493))
        private set

    var circleRadius by mutableDoubleStateOf(100.0)
        private set

    var filteredRestaurants by mutableStateOf<List<Boolean>>(emptyList())
        private set

    private var circleJob by mutableStateOf<Job?>(null)

    private var currentUser by mutableStateOf(User())

    var likedAndNew by mutableStateOf<List<Boolean>>(emptyList())
        private set

    private var startTime by mutableStateOf(Date())

    private suspend fun updateUserData(restaurants: List<Restaurant>) {
        val user = userUseCases.getUserObject()
        currentUser = user
        val newRestaurants = restaurantsUseCases.getIsNewRestaurantArray(restaurants)
        val potentialLikes = restaurantsUseCases.hasLikedCategoriesArray(restaurants, currentUser.preferences)
        val notLikedYet = restaurants.map { !currentUser.likes.contains(it.documentId)  }
        likedAndNew = newRestaurants.mapIndexed { index, new -> new && potentialLikes[index] && notLikedYet[index]}
    }

    private fun startTimer() {
        startTime = Date()
    }

    private fun sendEvent() {
        viewModelScope.launch(Dispatchers.IO) {
            val miliDifference = Date().time - startTime.time
            val timer = TimeUnit.MILLISECONDS.toSeconds(miliDifference)
            analyticsUseCases.sendScreenTimeEvent("map_screen", timer, currentUser.documentId)
        }
    }

    private fun sendFeatureInteractionEvent(featureName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            analyticsUseCases.sendFeatureInteraction(featureName, currentUser.documentId)
        }
    }


    fun onEvent(event: MapEvent) {
        when (event) {
            is MapEvent.PermissionGranted -> permissionAccepted()
            is MapEvent.PermissionDenied -> permissionDenied()
            is MapEvent.RadiusChanged -> changeCircleRadius(event.radius)
            is MapEvent.Closing -> cancelCircleFollowing()
            is MapEvent.PinClick -> downloadImage(state.restaurants, event.index, event.onGather)
            is MapEvent.ScreenOpened -> startTimer()
            is MapEvent.ScreenClosed -> sendEvent()
            is MapEvent.FeatureInteraction -> sendFeatureInteractionEvent(event.featureName)
        }
    }

    private fun permissionDenied() {
        state = state.copy(
            permission = false
        )
    }

    private fun changeCircleRadius(
        radius: Double
    ) {
        viewModelScope.launch(Dispatchers.Default) {
            val filteredRestaurantsCalc =
                restaurantsUseCases.getRestaurantsInRadius(state.restaurants, currentLocation, radius)
            circleRadius = radius
            filteredRestaurants = filteredRestaurantsCalc
        }
    }

    private fun cancelCircleFollowing() {
        circleJob?.cancel()
    }

    private fun permissionAccepted() {
        val startingLocation = getFirstLocation()
        val restaurants = getRestaurants()
        circleJob = viewModelScope.launch(Dispatchers.IO) {
            state = state.copy(
                startLocation = startingLocation.await(),
                permission = true,
            )
            initializeData(restaurants.await(), startingLocation.await())
            locationUseCases.getLocation.invoke().collect {
                if (isActive) {
                    //currentLocation = it?: currentLocation
                    filteredRestaurants = restaurantsUseCases.getRestaurantsInRadius(state.restaurants, currentLocation, circleRadius)
                }
            }
        }
    }

    private suspend fun initializeData(restaurants: List<Restaurant>, startingLocation: LatLng) {
        filteredRestaurants = restaurantsUseCases.getRestaurantsInRadius(restaurants, startingLocation, circleRadius)
        updateUserData(restaurants)
        state = state.copy(
            restaurants = restaurants,
            images = restaurants.map { null },
            isLoading = false
        )
        //currentLocation = startingLocation
    }


    private fun downloadImage(restaurants: List<Restaurant>, index: Int, onGather: suspend () -> Unit) {
        Log.d("DONITEST", "test")
        Log.d("DONITEST", "${state.images[index]}")
        if (state.images[index] == null) {
            viewModelScope.launch(Dispatchers.IO) {
                val bitmap = imageDownloadUseCases.downloadSingleImage(restaurants[index].imageUrl)
                val images = state.images.toMutableList()
                images[index] = bitmap
                state = state.copy(
                    images = images,
                )
                withContext(Dispatchers.Main) {
                    onGather()
                }
            }
        }
    }

    private fun getFirstLocation(): Deferred<LatLng> {
        return viewModelScope.async(Dispatchers.IO) {
            locationUseCases.getLocation.invoke().first() ?: state.startLocation
        }
    }

    private fun getRestaurants(): Deferred<List<Restaurant>> {
        return viewModelScope.async(Dispatchers.IO) {
            restaurantsUseCases.getRestaurants()
        }
    }




}