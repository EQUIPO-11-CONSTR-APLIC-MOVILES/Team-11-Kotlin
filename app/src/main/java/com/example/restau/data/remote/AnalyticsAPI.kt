package com.example.restau.data.remote

import com.example.restau.data.remote.dto.RestaurantData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AnalyticsAPI {

    @GET("like_review_week")
    suspend fun getLikeReviewWeek(): Response<List<RestaurantData>>

    @GET("reviewed_restaurant_percent")
    suspend fun getPercentageCompletion(
        @Query("userID") userID: String
    ): Response<Int>

    @GET("match-percentage")
    suspend fun getMatchPercentage(
        @Query("userID") userID: String,
        @Query("restaurantID") restaurantID: String
    ): Response<Float>

}