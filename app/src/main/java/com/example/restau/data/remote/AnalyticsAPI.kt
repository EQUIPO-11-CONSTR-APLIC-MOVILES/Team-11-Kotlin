package com.example.restau.data.remote

import com.example.restau.data.remote.dto.RestaurantData
import retrofit2.Response
import retrofit2.http.GET

interface AnalyticsAPI {

    @GET("like_review_week")
    suspend fun getLikeReviewWeek(): Response<List<RestaurantData>>

}