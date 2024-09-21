package com.example.restau.presentation.search

import com.example.restau.domain.model.Restaurant

sealed class SearchEvent {

    data class FilterEvent(val selectedFilter: Int): SearchEvent()

    data class ChangeNameEvent(val restaurantName: String): SearchEvent()

    data object ClearRecentRestaurantsEvent: SearchEvent()

    data class SaveRecentRestaurantEvent(val restaurantId: String): SearchEvent()

    data class SearchFilterEvent(val restaurantName: String, val restaurants: List<Restaurant>): SearchEvent()



}