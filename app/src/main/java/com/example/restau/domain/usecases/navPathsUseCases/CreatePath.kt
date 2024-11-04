package com.example.restau.domain.usecases.navPathsUseCases

import com.example.restau.domain.repository.NavPathsRepository

class CreatePath (private val navPathsRepository: NavPathsRepository) {
    suspend operator fun invoke(): String? {
        return navPathsRepository.createPath()
    }
}