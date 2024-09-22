package com.example.restau.domain.repository

import androidx.compose.ui.graphics.ImageBitmap

interface ImageRepository {

    suspend fun downloadImages(urlList: List<String>): List<ImageBitmap>

    suspend fun downloadAsBitmap(url: String): ImageBitmap?
}