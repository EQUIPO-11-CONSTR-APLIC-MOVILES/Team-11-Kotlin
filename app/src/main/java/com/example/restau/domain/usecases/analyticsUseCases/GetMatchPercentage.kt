package com.example.restau.domain.usecases.analyticsUseCases

import com.example.restau.domain.repository.AnalyticsRepository

class GetMatchPercentage(
    private val analyticsRepository: AnalyticsRepository
) {
    suspend operator fun invoke(userID: String, restaurantID: String): Float? {
        return analyticsRepository.getMatchPercentage(userID, restaurantID)
    }
}