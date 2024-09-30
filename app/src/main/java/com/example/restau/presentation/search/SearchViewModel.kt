package com.example.restau.presentation.search

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restau.domain.usecases.RestaurantUseCases
import com.example.restau.domain.usecases.RecentsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.activity.result.ActivityResultLauncher
import com.example.restau.domain.model.Restaurant

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val restaurantUseCases: RestaurantUseCases,
    private val recentsUseCases: RecentsUseCases
) : ViewModel() {

    var state by mutableStateOf(SearchState())
        private set

    var restaurantName by mutableStateOf("")
        private set


    init {
        getRestaurants()
        getRecentRestaurants()
    }

    fun onEvent(event: SearchEvent) {
        when(event) {
            is SearchEvent.SearchFilterEvent -> {
                getFilterRestaurantsByNameAndCategories(event.restaurantName, state.restaurants)
            }
            is SearchEvent.FilterEvent -> TODO()
            is SearchEvent.ChangeNameEvent -> {
                onRestaurantNameChange(event.restaurantName)
            }

            is SearchEvent.ClearRecentRestaurantsEvent -> {
                clearRecentRestaurants()
            }

            is SearchEvent.SaveRecentRestaurantEvent -> {
                saveRecentRestaurant(event.restaurantId)
            }

            is SearchEvent.VoiceRecognitionEvent -> {
                startVoiceRecognition(event.activity, event.speechRecognizerLauncher)
            }

            is SearchEvent.VoiceRecognitionChangeEvent -> {
                onRestaurantNameChange(event.spokenText)
                getFilterRestaurantsByNameAndCategories(event.spokenText, state.restaurants)
            }
        }
    }

    private fun onRestaurantNameChange(newName: String) {
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

    private fun saveRecentRestaurant(restaurantId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentRecents = recentsUseCases.getRecents().first() ?: emptySet()
            val updatedRecents = currentRecents + restaurantId
            recentsUseCases.saveRecents(updatedRecents)
            //Log.d("RecentsCheck", "Updated Recents: $updatedRecents")
        }
    }

    private fun clearRecentRestaurants() {
        viewModelScope.launch(Dispatchers.IO) {
            recentsUseCases.saveRecents(emptySet())

        }
    }

    private fun getFilterRestaurantsByNameAndCategories(restaurantName: String, restaurants: List<Restaurant>) {
        viewModelScope.launch(Dispatchers.IO) {
            val filteredRestaurants = restaurantUseCases.getFilterRestaurantsByNameAndCategories(restaurants, restaurantName)
            state = state.copy(filteredRestaurantsByNameAndCategories = filteredRestaurants)
        }
    }

    private fun startVoiceRecognition(activity: Activity, speechRecognizerLauncher: ActivityResultLauncher<Intent>) {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak the restaurant name or category")
        }
        speechRecognizerLauncher.launch(intent)
    }

}

