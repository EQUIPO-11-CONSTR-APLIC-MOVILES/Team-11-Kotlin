package com.example.restau.data.repository

import android.util.Log
import com.example.restau.domain.model.MenuItem
import com.example.restau.domain.repository.MenuItemsRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class MenuItemsRepositoryImpl(
    private val db: FirebaseFirestore
): MenuItemsRepository {

    private val TAG = "FIREBASE_MENUITEMS"

    override suspend fun getMenuItems(restaurantId: String): List<MenuItem> {
        val menuItems = mutableListOf<MenuItem>()
        try {
            val snapshot = db
                .collection("menu_items")
                .whereEqualTo("restaurantId", restaurantId)
                .get()
                .await()
            for (document in snapshot.documents) {
                document.toObject<MenuItem>()?.let {
                    menuItems.add(
                        it.copy(documentId = document.id)
                    )
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, e.toString())
        }
        return menuItems
    }

    override suspend fun getMenuItem(itemID: String): MenuItem {
        try{
            val snapshot = db
                .collection("menu_items")
                .document(itemID)
                .get()
                .await()

            return snapshot.toObject<MenuItem>()?.copy(documentId = snapshot.id) ?: MenuItem()
        } catch (e: Exception) {
            Log.w(TAG, e.toString())
            return MenuItem()
        }
    }
}