package com.example.restau.domain.usecases

class AnalyticsUseCases(
    val sendScreenTimeEvent: SendScreenTimeEvent,
    val sendFeatureInteraction: SendFeatureInteractionEvent
) {
}