package com.example.restau.domain.usecases.recentsUseCases

import com.example.restau.domain.repository.RecentsRepository
import kotlinx.coroutines.flow.Flow

class GetRecents(
    private val recentsRepository: RecentsRepository
) {

    operator fun invoke(): Flow<Set<String>> {
        return recentsRepository.readRecentsEntry()
    }

}