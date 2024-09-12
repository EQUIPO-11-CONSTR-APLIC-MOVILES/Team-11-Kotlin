package com.example.restau.presentation.home

import androidx.lifecycle.ViewModel
import com.example.restau.domain.usecases.RestaurantUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val restaurantUseCases: RestaurantUseCases
): ViewModel() {
}