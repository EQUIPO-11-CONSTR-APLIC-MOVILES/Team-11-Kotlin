package com.example.restau.presentation.navigator

sealed class NavigatorEvent {

    class SelectedChange(val selected: Int) : NavigatorEvent()

    class AuthCheck(var isSignedIn: Boolean) : NavigatorEvent()

}