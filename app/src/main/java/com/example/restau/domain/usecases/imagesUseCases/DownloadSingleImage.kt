package com.example.restau.domain.usecases.imagesUseCases

import androidx.compose.ui.graphics.ImageBitmap
import com.example.restau.domain.repository.ImageRepository

class DownloadSingleImage(
    private val imageRepository: ImageRepository
) {

    suspend operator fun invoke(url: String): ImageBitmap? {
        return imageRepository.downloadAsBitmap(url)
    }

}