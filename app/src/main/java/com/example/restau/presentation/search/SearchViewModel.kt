package com.example.restau.presentation.search

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.restau.domain.model.User
import com.example.restau.domain.usecases.analyticsUseCases.AnalyticsUseCases
import com.example.restau.domain.usecases.recentsUseCases.RecentsUseCases
import com.example.restau.domain.usecases.restaurantUseCases.RestaurantUseCases
import com.example.restau.domain.usecases.userUseCases.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.AndroidViewModel
import com.example.restau.domain.model.Restaurant
import com.example.restau.utils.getConnectivityAsStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val restaurantUseCases: RestaurantUseCases,
    private val recentsUseCases: RecentsUseCases,
    private val analyticsUseCases: AnalyticsUseCases,
    private val userUseCases: UserUseCases,
    private val application: Application,
    ) : AndroidViewModel(application) {

    val isConnected: StateFlow<Boolean> = application.getConnectivityAsStateFlow(viewModelScope)

    var state by mutableStateOf(SearchState())
        private set

    var restaurantName by mutableStateOf("")
        private set

    private var startTime by mutableStateOf(Date())

    var currentUser by mutableStateOf(User())
        private set

    var showFallback by mutableStateOf(false)
        private set

    var isNavigatingUserDetail by mutableStateOf(false)
        private set

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

    private fun sendSearchedCategoriesEvent(restaurantId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val restaurant = state.restaurants.find { it.documentId == restaurantId }
            if (restaurant != null) {
                analyticsUseCases.sendSearchedCategoriesEvent(restaurant.categories)
            }
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
                saveRecentRestaurant(event.restaurant)
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
            is SearchEvent.SearchedCategoriesEvent -> sendSearchedCategoriesEvent(event.restaurantId)
            SearchEvent.ScreenLaunched -> {
                getRestaurants()
                getRecentRestaurants()
                updateUser()
            }

            is SearchEvent.ChangeIsNavigatingUserDetail -> {
                onChangeisNavigatingUserDetail(event.isNavigating)
            }
        }
    }

    private fun onRestaurantNameChange(newName: String) {
        restaurantName = newName
    }

    private fun getRestaurants() {
        viewModelScope.launch(Dispatchers.IO) {
            val restaurants = restaurantUseCases.getRestaurants()
            if (!isConnected.value && restaurants.isEmpty()) {
                showFallback = true
            } else {
                showFallback = false
                state = state.copy(
                    restaurants = restaurants
                )
            }

            getRecentRestaurants()
        }
    }

    private fun getRecentRestaurants() {
        if (state.recentRestaurants.isEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                recentsUseCases.getRecents().collect { recentRestaurantsSet ->
                    val recentRestaurantsList = recentRestaurantsSet.toList()
                    state = state.copy(recentRestaurants = recentRestaurantsList)
                }
            }
        }
    }

    private fun saveRecentRestaurant(restaurant: Restaurant) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentRecents = recentsUseCases.getRecents().first() ?: emptySet<Restaurant>()
            val updatedRecents = currentRecents + restaurant
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

    private fun onChangeisNavigatingUserDetail(isNavigating: Boolean) {
        isNavigatingUserDetail = isNavigating
    }

}

