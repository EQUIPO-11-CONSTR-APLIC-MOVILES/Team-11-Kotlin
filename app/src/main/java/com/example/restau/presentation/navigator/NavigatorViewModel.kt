package com.example.restau.presentation.navigator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restau.domain.usecases.AuthUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NavigatorViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
): ViewModel() {


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

            is NavigatorEvent.AuthCheck -> {
                viewModelScope.launch {
                    AuthUseCases.isSignedIn = (getCurrentUserTokenID() != null)
                }
            }
        }
    }

    private suspend fun getCurrentUserTokenID(): String? {
        return withContext(Dispatchers.IO) {
            authUseCases.getCurrentUser()
        }
    }
}


