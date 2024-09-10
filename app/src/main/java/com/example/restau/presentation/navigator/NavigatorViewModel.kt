package com.example.restau.presentation.navigator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//@HiltViewModel
class NavigatorViewModel: ViewModel() {


    var selected by mutableIntStateOf(0)
        private set

    var showSplash by mutableStateOf(true)
        private set

    init {
        viewModelScope.launch {
            delay(700L)
            showSplash = false
        }
    }


    fun onEvent(event: NavigatorEvent) {
        when (event) {
            is NavigatorEvent.SelectedChange -> {
                selected = event.selected
            }
        }
    }

}