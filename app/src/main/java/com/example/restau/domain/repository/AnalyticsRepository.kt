package com.example.restau.domain.repository

import com.example.restau.data.remote.dto.RestaurantData

interface AnalyticsRepository {

    suspend fun getLikeReviewWeek(): List<RestaurantData>

    suspend fun getPercentageCompletion(userID:String): Int

}