package com.example.restau.domain.repository

import android.net.Uri
import com.example.restau.domain.model.User

interface UsersRepository {
    suspend fun getUser(email: String): User
    suspend fun submitLike(user: User)
    suspend fun saveTags(tags: List<String>, user: User)
    suspend fun setUserInfo(name: String, email: String, picLink: String, userID: String): Boolean
    suspend fun updateUserInfo (user: User): Boolean
    suspend fun updateReviewsAuthorInfo (documentId: String, authorName: String, authorPFP: String): Boolean
    suspend fun uploadProfileImage(user: User, uri: Uri): String?
}