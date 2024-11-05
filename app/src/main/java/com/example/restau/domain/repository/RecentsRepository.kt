package com.example.restau.domain.repository

import com.example.restau.domain.model.Restaurant
import kotlinx.coroutines.flow.Flow

interface RecentsRepository {
    
    suspend fun saveRecentsEntry(recents: Set<Restaurant>)

    fun readRecentsEntry(): Flow<Set<Restaurant>>
    
}