package com.example.restau.domain.usecases.analyticsUseCases

import com.example.restau.domain.repository.SearchedCategoriesRepository

class SendSearchedCategoriesEvent(
    private val searchedCategoriesRepository: SearchedCategoriesRepository
) {

    suspend operator fun invoke(categories: List<String>) {
        searchedCategoriesRepository.sendSearchedEvent(categories)
    }

}