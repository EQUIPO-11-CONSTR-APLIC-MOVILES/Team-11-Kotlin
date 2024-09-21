package com.example.restau.presentation.map

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restau.domain.model.Restaurant
import com.example.restau.domain.usecases.ImageDownloadUseCases
import com.example.restau.domain.usecases.LocationUseCases
import com.example.restau.domain.usecases.RestaurantUseCases
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val locationUseCases: LocationUseCases,
    private val restaurantsUseCases: RestaurantUseCases,
    private val imageDownloadUseCases: ImageDownloadUseCases
) : ViewModel() {

    var state by mutableStateOf(MapState())
        private set

    var currentLocation by mutableStateOf(LatLng(0.0, 0.0))
        private set

    var circleRadius by mutableDoubleStateOf(100.0)
        private set

    var filteredRestaurants by mutableStateOf<List<Boolean>>(emptyList())
        private set

    private var circleJob by mutableStateOf<Job?>(null)


    fun onEvent(event: MapEvent) {
        when (event) {
            is MapEvent.PermissionGranted -> permissionAccepted()
            is MapEvent.PermissionDenied -> permissionDenied()
            is MapEvent.RadiusChanged -> changeCircleRadius(event.radius)
            is MapEvent.Closing -> cancelCircleFollowing()
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
                    currentLocation = it?: currentLocation
                    filteredRestaurants = restaurantsUseCases.getRestaurantsInRadius(state.restaurants, currentLocation, circleRadius)
                }
            }
        }
    }

    private suspend fun initializeData(restaurants: List<Restaurant>, startingLocation: LatLng) {
        filteredRestaurants = restaurantsUseCases.getRestaurantsInRadius(restaurants, startingLocation, circleRadius)
        state = state.copy(
            restaurants = restaurants,
            images = downloadImages(restaurants).await(),
            isLoading = false
        )
        currentLocation = startingLocation
    }


    private fun downloadImages(restaurants: List<Restaurant>): Deferred<List<ImageBitmap>> {
        return viewModelScope.async(Dispatchers.IO) {
            val urlList = restaurants.map { it.imageUrl }
            imageDownloadUseCases.downloadImages(urlList)
        }
    }

    private fun getFirstLocation(): Deferred<LatLng> {
        return viewModelScope.async(Dispatchers.IO) {
            locationUseCases.getLocation.invoke().first() ?: state.startLocation
        }
    }

    private fun getRestaurants(): Deferred<List<Restaurant>> {
        return viewModelScope.async(Dispatchers.IO) {
            restaurantsUseCases.getOpenRestaurants()
        }
    }




}