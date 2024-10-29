package com.example.restau.domain.usecases.analyticsUseCases

import com.example.restau.data.remote.dto.RestaurantData
import com.example.restau.domain.repository.AnalyticsRepository

class GetLikeReviewWeek(
    private val analyticsRepository: AnalyticsRepository
) {

    suspend operator fun  invoke(): List<String> {
        return analyticsRepository.getLikeReviewWeek().take(3).map { it.name }
    }

}