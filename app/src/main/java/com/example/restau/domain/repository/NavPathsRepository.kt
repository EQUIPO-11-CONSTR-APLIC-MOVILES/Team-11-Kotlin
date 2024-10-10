package com.example.restau.domain.repository

interface NavPathsRepository {
    suspend fun createPath(): String?
    suspend fun updatePath(newScreenID: Int, pathID: String): Boolean
}