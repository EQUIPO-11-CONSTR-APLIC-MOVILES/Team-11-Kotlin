package com.example.restau.domain.usecases.analyticsUseCases

class AnalyticsUseCases(
    val sendScreenTimeEvent: SendScreenTimeEvent,
    val sendFeatureInteraction: SendFeatureInteractionEvent,
    val sendSearchedCategoriesEvent: SendSearchedCategoriesEvent
) {
}