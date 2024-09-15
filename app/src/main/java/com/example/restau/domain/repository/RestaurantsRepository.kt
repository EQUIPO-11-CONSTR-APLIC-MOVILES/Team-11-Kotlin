package com.example.restau.domain.repository

import com.example.restau.domain.model.Restaurant

interface RestaurantsRepository {

    suspend fun getAllRestaurants(): List<Restaurant>

    suspend fun getOpenRestaurants(day: String, time: Int): List<Restaurant>

}