package com.example.restau.presentation.home

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.restau.domain.model.Restaurant
import com.example.restau.domain.model.User
import com.example.restau.domain.usecases.analyticsUseCases.AnalyticsUseCases
import com.example.restau.domain.usecases.restaurantUseCases.RestaurantUseCases
import com.example.restau.domain.usecases.userUseCases.UserUseCases
import com.example.restau.utils.getConnectivityAsStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val restaurantUseCases: RestaurantUseCases,
    private val userUseCases: UserUseCases,
    private val analyticsUseCases: AnalyticsUseCases,
    private val application: Application,
): AndroidViewModel(application) {

    val isConnected: StateFlow<Boolean> = application.getConnectivityAsStateFlow(viewModelScope)

    var state by mutableStateOf(HomeState())
        private set

    var reload by mutableStateOf(false)
        private set

    var completion by mutableStateOf(0)
        private set

    var showFallback by mutableStateOf(false)
        private set

    var currentUser by mutableStateOf(User())
        private set

    private var startTime by mutableStateOf(Date())

    private fun updateUserAndData() {
        viewModelScope.launch(Dispatchers.IO) {
            val user = userUseCases.getUserObject()
            currentUser = user
            val restaurants = restaurantUseCases.getRestaurants()
            if (!isConnected.value && restaurants.isEmpty()) {
                showFallback = true
            } else {
                showFallback = false
                getRestaurants()
            }
        }
    }



    private fun startTimer() {
        startTime = Date()
    }

    private fun sendEvent() {
        viewModelScope.launch(Dispatchers.IO) {
            val miliDifference = Date().time - startTime.time
            val timer = TimeUnit.MILLISECONDS.toSeconds(miliDifference)
            analyticsUseCases.sendScreenTimeEvent("home_screen", timer, currentUser.documentId)
        }
    }

    private fun sendFeatureInteractionEvent(featureName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            analyticsUseCases.sendFeatureInteraction(featureName, currentUser.documentId)
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
            is HomeEvent.ScreenOpened -> {
                startTimer()
            }
            is HomeEvent.ScreenClosed -> {
                sendEvent()
            }
            is HomeEvent.ScreenLaunched -> {
                updateUserAndData()
                reload = !reload
            }

            is HomeEvent.FeatureInteraction -> sendFeatureInteractionEvent(event.featureName)
            is HomeEvent.LikeDateEvent -> sendLikeDateRestaurantEvent(event.restaurantId)
        }
    }


    private fun sendLikeDateRestaurantEvent(restaurantId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            analyticsUseCases.sendLikeDateRestaurantEvent(restaurantId)
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
            userUseCases.sendLike(currentUser)
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
            state = state.copy(nothingforyou = false, nothingOpen = false)
            when (state.selectedFilter) {
                0 -> {
                    restaurants = restaurantUseCases.getOpenRestaurants()
                    if (restaurants.isEmpty()) state = state.copy(nothingOpen = true)
                }
                1 -> {
                    restaurants = updateForYouData(restaurantUseCases.getRestaurants())
                    if (restaurants.isEmpty()) state = state.copy(nothingforyou = true)
                }
                2 -> {
                    restaurants = restaurantUseCases.getRestaurants()
                }
            }
            completion = analyticsUseCases.getPercentageCompletion(currentUser.documentId)
            updateRestaurantsState(restaurants)
        }
    }

    private suspend fun updateRestaurantsState(restaurants: List<Restaurant>) {
        val featuredNames = analyticsUseCases.getLikeReviewWeek()
        state = state.copy(
            restaurants = restaurants,
            isFeatured = restaurantUseCases.getFeaturedArray(restaurants, featuredNames),
            isNew = restaurantUseCases.getIsNewRestaurantArray(restaurants),
            isLiked = restaurantUseCases.getRestaurantsLiked(restaurants, currentUser.likes),
        )
    }

    private fun updateForYouData(restaurants: List<Restaurant>): List<Restaurant> {
        val potentialLikes = restaurantUseCases.hasLikedCategoriesArray(restaurants, currentUser.preferences)
        val notLikedYet = restaurants.map { !currentUser.likes.contains(it.documentId) }

        val forYouList = restaurants.filterIndexed { index, _ ->
             potentialLikes[index] && notLikedYet[index]
        }

        return forYouList
    }

}