package com.example.restau.domain.usecases.recentsUseCases

import com.example.restau.domain.model.Restaurant
import com.example.restau.domain.repository.RecentsRepository
import kotlinx.coroutines.flow.Flow

class GetRecents(
    private val recentsRepository: RecentsRepository
) {

    operator fun invoke(): Flow<Set<Restaurant>> {
        return recentsRepository.readRecentsEntry()
    }

}