package com.example.restau.data.repository

import android.util.Log
import com.example.restau.domain.repository.MapSearchTimesRepository
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

class MapSearchTimesRepositoryImpl(
    private val db: FirebaseFirestore
): MapSearchTimesRepository {

    private val TAG = "FIRESTORE_RESTAURANTS"

    override suspend fun sendMapSearchTimes(time: Timestamp) {
        val data = mapOf(
            "time" to time
        )
        db.collection("map_search_times")
            .add(data)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "Map search time event written with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding Map search time event", e)
            }
    }

}