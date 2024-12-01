package com.example.restau.presentation.menuDetail

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restau.domain.usecases.menuItemsUseCases.MenuItemsUseCases
import com.example.restau.utils.getConnectivityAsStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuDetailViewModel @Inject constructor(
    private val menuItemsUseCases: MenuItemsUseCases,
    application: Application
): ViewModel() {

    val isConnected: StateFlow<Boolean> = application.getConnectivityAsStateFlow(viewModelScope)

    var state by mutableStateOf(MenuDetailState())

    var showFallback by mutableStateOf(false)
        private set

    fun onEvent(event: MenuDetailEvent) {
        when (event) {
            is MenuDetailEvent.OnLaunch -> {
                onLaunch(event.itemID)
            }
        }
    }

    private fun onLaunch(itemID: String){
        viewModelScope.launch {
            try {

                val item = menuItemsUseCases.getMenuItem(itemID)
                state = state.copy(item = item)

                showFallback = !isConnected.value && item.documentId.isEmpty()

            } catch (e: Exception) {
                Log.e("MenuDetailViewModel", "Error during onLaunch", e)
            }
        }
    }
}