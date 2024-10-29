package com.example.restau.data.repository

import android.util.Log
import com.example.restau.data.remote.AnalyticsAPI
import com.example.restau.data.remote.dto.RestaurantData
import com.example.restau.domain.repository.AnalyticsRepository

class AnalyticsRepositoryImpl(
    private val analyticsAPI: AnalyticsAPI
): AnalyticsRepository {

    override suspend fun getLikeReviewWeek(): List<RestaurantData> {
        try {
            val response = analyticsAPI.getLikeReviewWeek()
            if (response.isSuccessful) {
                Log.d("DONITEST", response.body().toString())
                return (response.body()?: emptyList())
            }
            return (emptyList())
        } catch (e: Exception) {
            Log.e("ANALYTICSREPOSITORY", e.message.toString())
            return (emptyList())
        }
    }

}