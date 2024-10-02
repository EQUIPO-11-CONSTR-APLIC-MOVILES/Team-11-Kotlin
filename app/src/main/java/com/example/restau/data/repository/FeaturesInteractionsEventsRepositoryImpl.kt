package com.example.restau.data.repository

import android.util.Log
import com.example.restau.domain.model.FeatureInteractionEvent
import com.example.restau.domain.repository.FeaturesInteractionsEventsRepository
import com.google.firebase.firestore.FirebaseFirestore

class FeaturesInteractionsEventsRepositoryImpl(
    private val db: FirebaseFirestore
): FeaturesInteractionsEventsRepository {

    val TAG = "FEATURE_INTERACTION_EVENT_FIRESTORE"

    override suspend fun sendFeatureInteraction(featureInteractionEvent: FeatureInteractionEvent) {
        db.collection("features_interactions")
            .add(featureInteractionEvent)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "Feature interaction event written with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding feature interaction event", e)
            }
    }

}