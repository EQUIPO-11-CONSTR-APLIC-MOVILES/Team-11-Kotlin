package com.example.restau.data.repository

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.restau.R
import com.example.restau.domain.repository.ImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

class ImageRepositoryImpl(
    private val context: Context
): ImageRepository {

    override suspend fun downloadImages(urlList: List<String>): List<ImageBitmap> {
        val images = mutableListOf<ImageBitmap>()
        urlList.forEach {
            val bitmap =
                downloadAsBitmap(it)?:
                ContextCompat
                    .getDrawable(context, R.drawable.error)!!
                    .toBitmap()
                    .asImageBitmap()
            images.add(bitmap)
        }
        return images
    }

    override suspend fun downloadAsBitmap(
        url: String
    ): ImageBitmap? {
        return withContext(Dispatchers.IO) {
            try {
                return@withContext BitmapFactory.decodeStream(URL(url).openStream()).asImageBitmap()
            } catch (e: IOException) {
                e.printStackTrace()
                return@withContext null
            } catch (e: MalformedURLException) {
                e.printStackTrace()
                return@withContext null
            }
        }
    }

}