package com.example.restau.data.repository

import android.util.Log
import com.example.restau.domain.repository.NavPathsRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class NavPathsRepositoryImpl (
private val db: FirebaseFirestore
) : NavPathsRepository {

    private val screens = hashMapOf(
        0 to "Home",
        1 to "Random",
        2 to "Search",
        3 to "Liked",
        4 to "Map"
    )

    override suspend fun createPath(): String? {
        return try{
            val pathMap = hashMapOf(
                "path" to "Home"
            )

            db.collection("navigation_paths").add(pathMap).await().id
        } catch (e: Exception){
            Log.d("NavPathsRepository", "createPath:failure", e)
            null
        }
    }

    override suspend fun updatePath(newScreenID: Int, pathID: String): Boolean {
        return try {

            val oldPath = db.collection("navigation_paths").document(pathID).get().await().getString("path")

            val pathMap = hashMapOf(
                "path" to "$oldPath > ${screens[newScreenID]}"
            )

            db.collection("navigation_paths").document(pathID).set(pathMap).await()

            true
        } catch (e: Exception){
            Log.d("NavPathsRepository", "updatePath:failure", e)
            false
        }
    }

}