package com.example.restau.domain.model

data class Review(
    val documentId: String = "",
    val authorId: String = "",
    val authorName: String = "",
    val description: String = "",
    val rating: Int = 0,
    val restaurantId: String = ""
)
