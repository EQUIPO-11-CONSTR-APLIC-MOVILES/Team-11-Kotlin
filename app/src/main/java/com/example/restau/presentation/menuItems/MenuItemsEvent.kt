package com.example.restau.presentation.menuItems

sealed class MenuItemsEvent {

    data class OnLaunch(val restaurantId: String): MenuItemsEvent()

    data class ChangeIsNavigatingUserDetail(val isNavigating: Boolean): MenuItemsEvent()

}