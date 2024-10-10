package com.example.restau.domain.repository

interface SearchedCategoriesRepository {

    suspend fun sendSearchedEvent(searchedCategories: List<String>)

}