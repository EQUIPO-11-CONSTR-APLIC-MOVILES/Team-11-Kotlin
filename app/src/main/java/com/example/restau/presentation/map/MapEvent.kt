package com.example.restau.presentation.map

sealed class MapEvent {

    data object PermissionGranted: MapEvent()
    data class RadiusChanged(val radius: Double): MapEvent()
    data object PermissionDenied: MapEvent()
    data object Closing: MapEvent()

}