package com.example.restau.domain.usecases

import com.example.restau.domain.model.FeatureInteractionEvent
import com.example.restau.domain.repository.FeaturesInteractionsEventsRepository

class SendFeatureInteractionEvent(
    private val featuresInteractionsEventsRepository: FeaturesInteractionsEventsRepository
) {

    suspend operator fun invoke(NameFeatureInteraction: String, userId: String ) {
        val featureInteractionEvent = FeatureInteractionEvent(
            NameFeatureInteraction = NameFeatureInteraction,
            userId = userId
        )
        featuresInteractionsEventsRepository.sendFeatureInteraction(featureInteractionEvent)
    }

}