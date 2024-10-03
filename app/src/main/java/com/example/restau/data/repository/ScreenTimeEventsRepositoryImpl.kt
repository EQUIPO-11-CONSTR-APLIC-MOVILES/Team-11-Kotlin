package com.example.restau.data.repository

import android.util.Log
import com.example.restau.domain.model.ScreenTimeEvent
import com.example.restau.domain.repository.ScreenTimeEventsRepository
import com.google.firebase.firestore.FirebaseFirestore

class ScreenTimeEventsRepositoryImpl(
    private val db: FirebaseFirestore
): ScreenTimeEventsRepository {

    val TAG = "SCREEN_TIME_EVENT_FIRESTORE"

    override suspend fun sendScreenTimeEvent(screenTimeEvent: ScreenTimeEvent) {
        db.collection("screen_time_events")
            .add(screenTimeEvent)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "Screen time event written with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding screen time event", e)
            }
    }

}