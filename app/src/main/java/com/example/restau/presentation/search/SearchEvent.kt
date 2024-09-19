package com.example.restau.presentation.search

sealed class SearchEvent {

    data class FilterEvent(val selectedFilter: Int): SearchEvent()

}