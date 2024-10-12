package com.example.restau.data.repository

import android.util.Log
import com.example.restau.domain.repository.LikeDateRestaurantRepository
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date

class LikeDateRestaurantRepositoryImpl(
    private val db: FirebaseFirestore
): LikeDateRestaurantRepository {

    val TAG = "LIKE_DATE_RESTAURANT_EVENT_FIRESTORE"

    override suspend fun sendLikeDateRestaurant(restaurantId: String) {

        val transferMap = mapOf(
            "date" to Timestamp(Date()),
            "restaurantId" to restaurantId
        )

        db.collection("like_date_restaurant_event")
            .add(transferMap)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "like date restaurant event written with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding like date restaurant event", e)
            }
    }
}