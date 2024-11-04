package com.example.restau.data.repository

import android.util.Log
import com.example.restau.domain.repository.RandomReviewRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class RandomReviewRepositoryImpl(
    private val db: FirebaseFirestore
) : RandomReviewRepository {
    override suspend fun addRandomReview(): String? {
        return try {
            val id = db.collection("random_review").add(hashMapOf("left_review" to false)).await().id
            Log.d("RandomReviewRepository", "addRandomReview:success with id $id")
            id
        } catch (e: Exception) {
            Log.d("RandomReviewRepository", "addRandomReview:failure", e)
            null
        }
    }

    override suspend fun updateRandomReview(randomID: String): Boolean {
        return try {
            var updated = false
            if (randomID.isNotEmpty()) {
                db.collection("random_review").document(randomID).update("left_review", true).await()
                updated = true
            }
            updated
        } catch (e: Exception) {
            Log.d("RandomReviewRepository", "updateRandomReview:failure", e)
            false
        }
    }
}