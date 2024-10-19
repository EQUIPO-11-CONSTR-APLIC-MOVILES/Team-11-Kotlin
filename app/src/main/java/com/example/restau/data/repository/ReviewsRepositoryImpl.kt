package com.example.restau.data.repository

import android.util.Log
import com.example.restau.domain.model.Review
import com.example.restau.domain.repository.ReviewsRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class ReviewsRepositoryImpl(
    private val db: FirebaseFirestore
): ReviewsRepository {

    private val TAG = "FIRESTORE_REVIEWS"

    override suspend fun getRestaurantsReviews(restaurantId: String): List<Review> {
        val reviews = mutableListOf<Review>()
        try {
            val snapshot = db
                .collection("reviews")
                .whereEqualTo("restaurantId", restaurantId)
                .orderBy("rating")
                .get()
                .await()
            for (document in snapshot.documents) {
                document.toObject<Review>()?.let {
                    reviews.add(
                        it.copy(documentId = document.id)
                    )
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, e.toString())
        }
        return reviews
    }

}