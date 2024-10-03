package com.example.restau.presentation.navigator

import android.content.Context

sealed class NavigatorEvent {

    class SelectedChange(val selected: Int) : NavigatorEvent()
    class OnStart(val context: Context) : NavigatorEvent()

}