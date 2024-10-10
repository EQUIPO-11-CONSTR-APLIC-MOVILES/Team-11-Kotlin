package com.example.restau.domain.usecases

import com.example.restau.domain.repository.NavPathsRepository

class UpdatePath (private val navPathsRepository: NavPathsRepository) {
    suspend operator fun invoke(newScreenID: Int, pathID: String): Boolean{
        return navPathsRepository.updatePath(newScreenID, pathID)
    }
}