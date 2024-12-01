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

    override suspend fun getPercentageCompletion(userID: String): Int {
        try {
            val response = analyticsAPI.getPercentageCompletion(userID)
            if (response.isSuccessful) {
                Log.d("DONITEST", response.body().toString())
                return (response.body()?: 0)
            }
            return (0)
        } catch (e: Exception) {
            Log.e("ANALYTICSREPOSITORY", e.message.toString())
            return (0)
        }
    }

    override suspend fun getMatchPercentage(userID: String, restaurantID: String): Float?{
        try {
            val response = analyticsAPI.getMatchPercentage(userID, restaurantID)
            if (response.isSuccessful) {
                return (response.body()?: 0.0f)
            }
            return (null)
        } catch (e: Exception) {
            Log.e("ANALYTICSREPOSITORY", e.message.toString())
            return (null)
        }
    }

}