package com.example.restau.presentation.map

sealed class MapEvent {

    data object PermissionGranted: MapEvent()
    data object PermissionRevoked: MapEvent()

}