package com.example.restau.presentation.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restau.domain.usecases.DateTimeUseCases
import com.example.restau.domain.usecases.RestaurantUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val restaurantUseCases: RestaurantUseCases,
    private val dateTimeUseCases: DateTimeUseCases
): ViewModel() {

    var state by mutableStateOf(HomeState())
        private set

    init {
        getRestaurants()
    }


    fun onEvent(event: HomeEvent) {
        when(event) {
            is HomeEvent.FilterEvent -> {
                state = state.copy(
                    selectedFilter = event.selectedFilter,
                    restaurants = emptyList()
                )
                getRestaurants()
            }
        }
    }

    private fun getRestaurants() {
        viewModelScope.launch(Dispatchers.IO) {
            when (state.selectedFilter) {
                0 -> {
                    val day = dateTimeUseCases.getCurrentDay()
                    val time = dateTimeUseCases.getCurrentTime()
                    state = state.copy(restaurants = restaurantUseCases.getOpenRestaurants(day, time))
                }

                1 -> {
                    //TODO
                    state = state.copy(restaurants = restaurantUseCases.getRestaurants())

                }
                2 -> {
                    state = state.copy(restaurants = restaurantUseCases.getRestaurants())
                }
            }
            state = state.copy(
                isNew = restaurantUseCases.getIsNewRestaurantArray(state.restaurants)
            )
            Log.d("DONITEST", state.isNew.toString())
        }
    }

}