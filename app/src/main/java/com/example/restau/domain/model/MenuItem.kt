package com.example.restau.domain.model

data class MenuItem(
    val documentId: String = "",
    val name: String = "",
    val description: String = "",
    val price: Int = 0,
    val imageUrl: String = "",
    val restaurantId: String = ""
)
