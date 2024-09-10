package com.example.restau.domain.repository

import kotlinx.coroutines.flow.Flow

interface RecentsRepository {
    
    suspend fun saveRecentsEntry(recents: Set<String>)

    fun readRecentsEntry(): Flow<Set<String>>
    
}