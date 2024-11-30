package com.example.restau.presentation.menuDetail

sealed class MenuDetailEvent {
    data class OnLaunch(val itemID: String): MenuDetailEvent()
}