package com.example.restau.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restau.domain.model.Restaurant
import com.example.restau.domain.usecases.RestaurantUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val restaurantUseCases: RestaurantUseCases
): ViewModel() {

    var state by mutableStateOf(HomeState())
        private set

    init {
        getRestaurants()
    }


    fun onEvent(event: HomeEvent) {
        when(event) {
            is HomeEvent.FilterEvent -> {
                filterChange(event.selectedFilter)
            }
        }
    }

    private fun filterChange(selectedFilter: Int) {
        state = state.copy(
            selectedFilter = selectedFilter,
            restaurants = emptyList(),
            nothingOpen = false
        )
        getRestaurants()
    }

    private fun getRestaurants() {
        viewModelScope.launch(Dispatchers.IO) {
            var restaurants = emptyList<Restaurant>()
            when (state.selectedFilter) {
                0 -> {
                    restaurants = restaurantUseCases.getOpenRestaurants()
                    if (restaurants.isEmpty()) state = state.copy(nothingOpen = true)
                }
                1 -> {
                    //TODO
                    restaurants = restaurantUseCases.getRestaurants()
                }
                2 -> {
                    restaurants = restaurantUseCases.getRestaurants()
                }
            }
            state = state.copy(
                restaurants = restaurants,
                isNew = restaurantUseCases.getIsNewRestaurantArray(restaurants),
            )
        }
    }
}