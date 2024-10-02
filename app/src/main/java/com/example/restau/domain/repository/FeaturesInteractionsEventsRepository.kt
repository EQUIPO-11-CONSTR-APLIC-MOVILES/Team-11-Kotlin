package com.example.restau.domain.repository

import com.example.restau.domain.model.FeatureInteractionEvent

interface FeaturesInteractionsEventsRepository {

    suspend fun sendFeatureInteraction(featureInteractionEvent: FeatureInteractionEvent)

}