package com.example.restau.domain.usecases

import androidx.compose.ui.graphics.ImageBitmap
import com.example.restau.domain.repository.ImageRepository

class DownloadImages(
    private val imageRepository: ImageRepository
) {

    suspend operator fun invoke(urlList: List<String>): List<ImageBitmap> {
        return imageRepository.downloadImages(urlList)
    }

}