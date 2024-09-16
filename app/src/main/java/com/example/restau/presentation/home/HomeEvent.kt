package com.example.restau.presentation.home

sealed class HomeEvent {

    data class FilterEvent(val selectedFilter: Int): HomeEvent()

}