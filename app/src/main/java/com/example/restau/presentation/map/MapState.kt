package com.example.restau.presentation.map

import com.example.restau.domain.model.Restaurant
import com.google.android.gms.maps.model.LatLng

data class MapState(
    val startLocation: LatLng = LatLng(4.603096177609384, -74.06584744436493),
    val permissionsGranted: Boolean = false,
    val restaurants: List<Restaurant> = emptyList(),
    val circleLocation: LatLng = LatLng(4.603096177609384, -74.06584744436493),
    val circleRadius: Double = 100.0
)