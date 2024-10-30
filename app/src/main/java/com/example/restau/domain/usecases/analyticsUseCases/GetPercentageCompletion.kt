package com.example.restau.domain.usecases.analyticsUseCases

import com.example.restau.domain.repository.AnalyticsRepository

class GetPercentageCompletion(
    private val analyticsRepository: AnalyticsRepository
) {

    suspend operator fun invoke(userID: String): Int {
        return analyticsRepository.getPercentageCompletion(userID)
    }

}