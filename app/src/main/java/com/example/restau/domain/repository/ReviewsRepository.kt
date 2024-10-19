package com.example.restau.domain.repository

import com.example.restau.domain.model.Review

interface ReviewsRepository {

    suspend fun getRestaurantsReviews(restaurantId: String): List<Review>

}