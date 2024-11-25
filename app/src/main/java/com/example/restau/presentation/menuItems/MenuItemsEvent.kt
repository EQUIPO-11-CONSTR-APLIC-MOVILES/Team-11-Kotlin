package com.example.restau.presentation.menuItems

sealed class MenuItemsEvent {

    data class OnLaunch(val restaurantId: String): MenuItemsEvent()

}