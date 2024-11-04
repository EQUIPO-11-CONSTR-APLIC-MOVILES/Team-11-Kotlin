package com.example.restau.domain.repository

interface RandomReviewRepository {
    suspend fun addRandomReview(): String?
    suspend fun updateRandomReview(randomID: String): Boolean
}