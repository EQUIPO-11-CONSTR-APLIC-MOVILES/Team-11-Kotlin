package com.example.restau.data.repository

import android.net.Uri
import android.util.Log
import com.example.restau.domain.model.User
import com.example.restau.domain.repository.UsersRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID
import android.content.Context
import com.google.gson.Gson



class UsersRepositoryImpl(
    private val db: FirebaseFirestore,
    private val context: Context
): UsersRepository {

    private val TAG = "FIRESTORE_USERS"

    override suspend fun getUser(email: String): User {
        val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val userJson = sharedPreferences.getString(email, null)
        var user: User

        if (userJson != null) {
            user = Gson().fromJson(userJson, User::class.java)
        } else {
            try {
                val snapshot = db
                    .collection("users")
                    .whereEqualTo("email", email)
                    .get()
                    .await()
                user = User()
                for (document in snapshot.documents) {
                    document.toObject<User>()?.let {
                        user = it.copy(documentId = document.id)
                    }
                }

                val editor = sharedPreferences.edit()
                editor.putString(email, Gson().toJson(user))
                editor.apply()
            } catch (e: Exception) {
                Log.w(TAG, e.toString())
                user = User()
            }
        }
        Log.d("DONITEST", user.toString())
        return user
    }

    override suspend fun submitLike(user: User) {
        val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        Log.d("DONITEST", "SUBMIT LIKE")
        try {

            Log.d("DONITEST", user.toString())
            val editor = sharedPreferences.edit()
            editor.putString(user.email, Gson().toJson(user))
            editor.apply()

            val dtUser = hashMapOf(
                "likes" to user.likes,
            )

            db.collection("users")
                .document(user.documentId)
                .set(dtUser, SetOptions.merge())
                .await()



        } catch (e: Exception) {
            Log.w("DONITEST", e.toString())
            Log.w(TAG, e.toString())
        }
    }

    override suspend fun saveTags(tags: List<String>, user: User) {
        val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        try {

            val updatedUser = user.copy(preferences = tags)

            val editor = sharedPreferences.edit()
            editor.putString(user.email, Gson().toJson(updatedUser))
            editor.apply()

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

    override suspend fun updateUserInfo(user: User): Boolean {
        return try {
            val info = mapOf(
                "name" to user.name,
                "profilePic" to user.profilePic
            )

            db.collection("users").document(user.documentId).set(info, SetOptions.merge()).await()

            val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString(user.email, Gson().toJson(user))
            editor.apply()

            true
        } catch (e: Exception) {
            Log.e("UsersRepository", "updateUserInfo: failure", e)
            false
        }
    }

    override suspend fun updateReviewsAuthorInfo(
        documentId: String,
        authorName: String,
        authorPFP: String
    ): Boolean {
        return try {
            val reviewsSnapshot = db.collection("reviews")
                .whereEqualTo("authorId", documentId)
                .get()
                .await()

            for (document in reviewsSnapshot.documents) {
                val reviewRef = db.collection("reviews").document(document.id)
                val updatedData = mapOf(
                    "authorName" to authorName,
                    "authorPFP" to authorPFP
                )

                reviewRef.set(updatedData, SetOptions.merge()).await()
            }

            Log.d("UsersRepository", "Reviews updated successfully for authorId: $documentId")
            true
        } catch (e: Exception) {
            Log.e("UsersRepository", "updateReviewsAuthorInfo: failure", e)
            false
        }
    }

    override suspend fun uploadProfileImage(user: User, uri: Uri): String? {
        return try {
            val storageRef = FirebaseStorage.getInstance().reference
                .child("profilePics/${UUID.randomUUID()}.jpg")

            val uploadTask = storageRef.putFile(uri).await()

            val downloadUrl = storageRef.downloadUrl.await().toString()

            downloadUrl
        } catch (e: Exception) {
            Log.e("UsersRepository", "uploadProfileImage: failure", e)
            null
        }
    }


}