package com.example.restau.presentation.map

sealed class MapEvent {

    data object PermissionGranted: MapEvent()
    data class RadiusChanged(val radius: Double): MapEvent()
    data class PinClick(val index: Int, val onGather: suspend () -> Unit): MapEvent()
    data object PermissionDenied: MapEvent()
    data object Closing: MapEvent()
    data object ScreenOpened: MapEvent()
    data object ScreenClosed: MapEvent()

}