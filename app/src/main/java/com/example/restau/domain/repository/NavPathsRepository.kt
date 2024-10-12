package com.example.restau.domain.repository

interface NavPathsRepository {
    suspend fun createPath(): String?
    suspend fun updatePath(screensIDs: List<Int>, pathID: String): Boolean
}