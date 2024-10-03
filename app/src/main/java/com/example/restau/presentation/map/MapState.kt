package com.example.restau.presentation.map

import androidx.compose.ui.graphics.ImageBitmap
import com.example.restau.domain.model.Restaurant
import com.google.android.gms.maps.model.LatLng

data class MapState(
    val restaurants: List<Restaurant> = emptyList(),
    val startLocation: LatLng = LatLng(4.603096177609384, -74.06584744436493),
    val isLoading: Boolean = true,
    val permission: Boolean = true,
    val images: List<ImageBitmap?> = emptyList(),
    val idIndexMap: Map<String, Int> = emptyMap(),
)