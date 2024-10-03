package com.example.restau.domain.repository

interface TagsRepository {
    suspend fun getTags(): Map<String, Pair<Int, List<String>>>
}