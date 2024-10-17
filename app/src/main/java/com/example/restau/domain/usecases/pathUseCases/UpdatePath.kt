package com.example.restau.domain.usecases.pathUseCases

import com.example.restau.domain.repository.NavPathsRepository

class UpdatePath (private val navPathsRepository: NavPathsRepository) {
    suspend operator fun invoke(screensIDs: List<Int>, pathID: String): Boolean{
        return navPathsRepository.updatePath(screensIDs, pathID)
    }
}