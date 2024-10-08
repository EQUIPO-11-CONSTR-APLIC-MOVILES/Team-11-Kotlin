package com.example.restau.data.repository

import android.util.Log
import com.example.restau.domain.model.Restaurant
import com.example.restau.domain.repository.RestaurantsRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class RestaurantsRepositoryImpl(
    private val db: FirebaseFirestore
): RestaurantsRepository {

    private val TAG = "FIRESTORE_RESTAURANTS"

    override suspend fun getAllRestaurants(): List<Restaurant> {
        val restaurants = mutableListOf<Restaurant>()
        try {
            val snapshot = db
                .collection("restaurants")
                .orderBy("averageRating")
                .get()
                .await()
            for (document in snapshot.documents) {
                document.toObject<Restaurant>()?.let {
                    restaurants.add(
                        it.copy(documentId = document.id)
                    )
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, e.toString())
        }
        return restaurants
    }

    override suspend fun getOpenRestaurants(day: String, time: Int): List<Restaurant> {
        val restaurants = mutableListOf<Restaurant>()
        try {
            val snapshot = db
                .collection("restaurants")
                .whereLessThanOrEqualTo("schedule.${day}.start", time)
                .whereGreaterThanOrEqualTo("schedule.${day}.end", time)
                .orderBy("averageRating")
                .get()
                .await()
            for (document in snapshot.documents) {
                document.toObject<Restaurant>()?.let {
                    restaurants.add(
                        it.copy(documentId = document.id)
                    )
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, e.toString())
        }
        return restaurants
    }

}