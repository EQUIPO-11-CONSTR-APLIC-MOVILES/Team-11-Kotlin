package com.example.restau.presentation.search

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.example.restau.domain.model.Restaurant
import com.example.restau.presentation.home.HomeEvent

sealed class SearchEvent {

    data class FilterEvent(val selectedFilter: Int): SearchEvent()

    data class ChangeNameEvent(val restaurantName: String): SearchEvent()

    data object ClearRecentRestaurantsEvent: SearchEvent()

    data object ScreenOpened: SearchEvent()

    data object ScreenClosed: SearchEvent()

    data class FeatureInteraction(val featureName: String) : SearchEvent()

    data class SaveRecentRestaurantEvent(val restaurant: Restaurant): SearchEvent()

    data class SearchFilterEvent(val restaurantName: String): SearchEvent()

    data class VoiceRecognitionChangeEvent(val spokenText: String): SearchEvent()

    data class VoiceRecognitionEvent(val activity: Activity, val speechRecognizerLauncher: ActivityResultLauncher<Intent>): SearchEvent()

    data class SearchedCategoriesEvent(val restaurantId: String): SearchEvent()

    data object ScreenLaunched: SearchEvent()

    data class ChangeIsNavigatingUserDetail(val isNavigating: Boolean): SearchEvent()

}