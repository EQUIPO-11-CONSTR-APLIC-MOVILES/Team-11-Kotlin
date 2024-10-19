package com.example.restau.presentation.random

sealed class RandomEvent {

    data object ScreenOpened: RandomEvent()
    data object  ScreenClosed: RandomEvent()
    data object  ScreenLaunched: RandomEvent()



}