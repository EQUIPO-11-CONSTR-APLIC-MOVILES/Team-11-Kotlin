package com.example.restau.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restau.domain.model.Restaurant
import com.example.restau.domain.model.User
import com.example.restau.domain.usecases.RestaurantUseCases
import com.example.restau.domain.usecases.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val restaurantUseCases: RestaurantUseCases,
    private val userUseCases: UserUseCases
): ViewModel() {

    var state by mutableStateOf(HomeState())
        private set

    var currentUser by mutableStateOf(User())
        private set

    init {
        updateUserAndData()
    }

    private fun updateUserAndData() {
        viewModelScope.launch(Dispatchers.IO) {
            val user = userUseCases.getUserObject()
            currentUser = user
            getRestaurants()
            currentUser.documentId != ""
        }
    }


    fun onEvent(event: HomeEvent) {
        when(event) {
            is HomeEvent.FilterEvent -> {
                filterChange(event.selectedFilter)
            }
            is HomeEvent.SendLike -> {
                modifyLike(event.documentId, event.delete)
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
            userUseCases.sendLike(documentId, currentUser)
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