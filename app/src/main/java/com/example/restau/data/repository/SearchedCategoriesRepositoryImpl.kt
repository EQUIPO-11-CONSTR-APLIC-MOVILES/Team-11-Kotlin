package com.example.restau.data.repository

import android.util.Log
import com.example.restau.domain.repository.SearchedCategoriesRepository
import com.google.firebase.firestore.FirebaseFirestore

class SearchedCategoriesRepositoryImpl(
    private val db: FirebaseFirestore
): SearchedCategoriesRepository {

    val TAG = "SEARCHED_CATEGORIES_EVENT_FIRESTORE"

    override suspend fun sendSearchedEvent(searchedCategories: List<String>) {
        val sendItem = searchedCategories.associateWith {
            null
        }

        db.collection("restaurant_search_types")
            .add(sendItem)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "Searched categories event written with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding searched categories event", e)
            }
    }

}