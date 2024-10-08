package com.example.restau.presentation.search

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restau.domain.model.User
import com.example.restau.domain.usecases.AnalyticsUseCases
import com.example.restau.domain.usecases.RecentsUseCases
import com.example.restau.domain.usecases.RestaurantUseCases
import com.example.restau.domain.usecases.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import androidx.activity.result.ActivityResultLauncher
import com.example.restau.domain.model.Restaurant

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val restaurantUseCases: RestaurantUseCases,
    private val recentsUseCases: RecentsUseCases,
    private val analyticsUseCases: AnalyticsUseCases,
    private val userUseCases: UserUseCases
) : ViewModel() {

    var state by mutableStateOf(SearchState())
        private set

    var restaurantName by mutableStateOf("")
        private set

    private var startTime by mutableStateOf(Date())

    var currentUser by mutableStateOf(User())
        private set

    init {
        getRestaurants()
        getRecentRestaurants()
        updateUser()
    }

    private fun updateUser() {
        viewModelScope.launch(Dispatchers.IO) {
            val user = userUseCases.getUserObject()
            currentUser = user
        }
    }

    private fun startTimer() {
        startTime = Date()
    }

    private fun sendEvent() {
        viewModelScope.launch(Dispatchers.IO) {
            val miliDifference = Date().time - startTime.time
            val timer = TimeUnit.MILLISECONDS.toSeconds(miliDifference)
            analyticsUseCases.sendScreenTimeEvent("search_screen", timer, currentUser.documentId)
        }
    }

    private fun sendFeatureInteractionEvent(featureName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            analyticsUseCases.sendFeatureInteraction(featureName, currentUser.documentId)
        }
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
            is SearchEvent.ScreenOpened -> startTimer()
            is SearchEvent.ScreenClosed -> sendEvent()
            is SearchEvent.FeatureInteraction -> sendFeatureInteractionEvent(event.featureName)
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

