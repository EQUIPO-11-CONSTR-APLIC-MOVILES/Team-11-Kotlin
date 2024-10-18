package com.example.restau.domain.repository

interface LikeDateRestaurantRepository {

    suspend fun sendLikeDateRestaurant(restaurantId: String)

}