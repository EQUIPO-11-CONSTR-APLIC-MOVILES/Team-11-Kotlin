package com.example.restau.domain.usecases

import com.example.restau.domain.repository.NavPathsRepository

class UpdatePath (private val navPathsRepository: NavPathsRepository) {
    suspend operator fun invoke(screensIDs: List<Int>, pathID: String?): Boolean{
        if (pathID == null) return false
        return navPathsRepository.updatePath(screensIDs, pathID)
    }
}