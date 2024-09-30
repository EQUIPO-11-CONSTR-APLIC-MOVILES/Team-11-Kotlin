package com.example.restau.presentation.search

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.example.restau.domain.model.Restaurant

sealed class SearchEvent {

    data class FilterEvent(val selectedFilter: Int): SearchEvent()

    data class ChangeNameEvent(val restaurantName: String): SearchEvent()

    data object ClearRecentRestaurantsEvent: SearchEvent()

    data class SaveRecentRestaurantEvent(val restaurantId: String): SearchEvent()

    data class SearchFilterEvent(val restaurantName: String): SearchEvent()

    data class VoiceRecognitionChangeEvent(val spokenText: String): SearchEvent()

    data class VoiceRecognitionEvent(val activity: Activity, val speechRecognizerLauncher: ActivityResultLauncher<Intent>): SearchEvent()



}