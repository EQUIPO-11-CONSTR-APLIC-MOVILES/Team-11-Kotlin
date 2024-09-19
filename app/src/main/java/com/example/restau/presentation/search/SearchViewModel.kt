package com.example.restau.presentation.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restau.domain.usecases.RestaurantUseCases
import com.example.restau.domain.usecases.RecentsUseCases
import com.example.restau.presentation.search.SearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.util.Log

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val restaurantUseCases: RestaurantUseCases,
    private val recentsUseCases: RecentsUseCases
) : ViewModel() {

    var state by mutableStateOf(SearchState())
        private set

    var restaurantName by mutableStateOf("")


    init {
        getRestaurants()
        getRecentRestaurants()
    }
    fun onRestaurantNameChange(newName: String) {
        restaurantName = newName
    }

    private fun getRestaurants() {
        viewModelScope.launch(Dispatchers.IO) {
            val restaurants = restaurantUseCases.getRestaurants()
            state = state.copy(restaurants = restaurants)

            getRecentRestaurants()
        }
    }

    private fun getRecentRestaurants() {
        if (state.recentRestaurants.isEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                recentsUseCases.getRecents().collect { recentIds ->
                    // Filtrar la lista de restaurantes para obtener los recientes
                    val recentRestaurants = state.restaurants.filter { it.documentId in recentIds }
                    state = state.copy(recentRestaurants = recentRestaurants)
                }
            }
        }
    }

    fun saveRecentRestaurant(restaurantId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentRecents = recentsUseCases.getRecents().first() ?: emptySet()
            val updatedRecents = currentRecents + restaurantId
            recentsUseCases.saveRecents(updatedRecents)
            //Log.d("RecentsCheck", "Updated Recents: $updatedRecents")
        }
    }

    fun clearRecentRestaurants() {
        viewModelScope.launch(Dispatchers.IO) {
            recentsUseCases.saveRecents(emptySet())

        }
    }
}

