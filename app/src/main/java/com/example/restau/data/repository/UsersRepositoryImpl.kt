package com.example.restau.data.repository

import android.util.Log
import com.example.restau.domain.model.User
import com.example.restau.domain.repository.UsersRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class UsersRepositoryImpl(
    private val db: FirebaseFirestore
): UsersRepository {

    private val TAG = "FIRESTORE_USERS"

    override suspend fun getUser(email: String): User {
        var user = User()
        try {
            val snapshot = db
                .collection("users")
                .whereEqualTo("email", email)
                .get()
                .await()
            for (document in snapshot.documents) {
                document.toObject<User>()?.let {
                    user = it.copy(documentId = document.id)
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, e.toString())
        }
        return user
    }

    override suspend fun submitLike(user: User) {
        try {
            val dtUser = hashMapOf(
                "likes" to user.likes,
            )
            db.collection("users")
                .document(user.documentId)
                .set(dtUser, SetOptions.merge())
                .await()

        } catch (e: Exception) {
            Log.w(TAG, e.toString())
        }
    }

    override suspend fun saveTags(tags: List<String>, user: User) {
        try {
            val dtUser = hashMapOf(
                "preferences" to tags,
            )

            db.collection("users")
                .document(user.documentId)
                .set(dtUser, SetOptions.merge())
                .await()

        } catch (e: Exception) {
            Log.w(TAG, e.toString())
        }
    }

    override suspend fun setUserInfo(name: String, email: String, picLink: String, userID: String): Boolean {
        return try {
            val info = hashMapOf(
                "name" to name,
                "email" to email,
                "profilePic" to picLink
            )

            Log.d("UsersRepository", "ID: $userID")

            db.collection("users").document(userID).set(info).await()

            true
        } catch (e: Exception) {
            Log.e("UsersRepository", "setUserInfo: failure", e)
            false
        }
    }
}