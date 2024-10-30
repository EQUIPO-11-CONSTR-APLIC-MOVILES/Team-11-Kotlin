package com.example.restau.domain.usecases.recentsUseCases

import com.example.restau.domain.model.Restaurant
import com.example.restau.domain.repository.RecentsRepository

class SaveRecents(
    private val recentsRepository: RecentsRepository
) {

    suspend operator fun invoke(recents: Set<Restaurant>) {
        return recentsRepository.saveRecentsEntry(recents)
    }

}