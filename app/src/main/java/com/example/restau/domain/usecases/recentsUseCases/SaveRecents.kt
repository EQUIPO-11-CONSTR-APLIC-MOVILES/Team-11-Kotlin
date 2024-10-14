package com.example.restau.domain.usecases.recentsUseCases

import com.example.restau.domain.repository.RecentsRepository

class SaveRecents(
    private val recentsRepository: RecentsRepository
) {

    suspend operator fun invoke(recents: Set<String>) {
        return recentsRepository.saveRecentsEntry(recents)
    }

}