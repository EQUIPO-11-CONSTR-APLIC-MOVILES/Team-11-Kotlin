package com.example.restau.presentation.map

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restau.domain.usecases.GetLocation
import com.example.restau.domain.usecases.RestaurantUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getLocation: GetLocation,
    private val restaurantsUseCases: RestaurantUseCases
) : ViewModel() {

    var state by mutableStateOf(MapState())
        private set

    private var circleJob by mutableStateOf<Job?>(null)


    fun onEvent(event: MapEvent) {
        when (event) {
            is MapEvent.PermissionGranted -> permissionAccepted()
            is MapEvent.PermissionRevoked -> permissionRevoked()
        }
    }

    private fun permissionRevoked() {
        state = state.copy(
            permissionsGranted = false
        )
    }

    fun cancelCircleFollowing() {
        circleJob?.cancel()
    }

    private fun permissionAccepted() {
        cancelCircleFollowing()
        viewModelScope.launch(Dispatchers.IO) {
          val startingLocation = getLocation.invoke().first()
          val restaurants = restaurantsUseCases.getOpenRestaurants()
          state = state.copy(
              permissionsGranted = true,
              restaurants = restaurants,
              startLocation = startingLocation?: state.startLocation
          )
        }
        circleJob = viewModelScope.launch(Dispatchers.IO) {
            getLocation.invoke().collect {
                if (isActive) {
                    state = state.copy(
                        circleLocation = it?: state.circleLocation
                    )
                }
            }
        }

    }
}