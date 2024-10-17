package com.example.restau.presentation.restaurant

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RestaurantViewModel @Inject constructor(

): ViewModel(){

    fun onEvent(event: RestaurantEvent) {
        when (event) {
            is RestaurantEvent.ScreenLaunched -> {
            }
        }
    }
}