package com.example.restau.presentation.menuItems

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.restau.domain.model.User
import com.example.restau.domain.usecases.menuItemsUseCases.MenuItemsUseCases
import com.example.restau.domain.usecases.userUseCases.UserUseCases
import com.example.restau.utils.getConnectivityAsStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuItemsViewModel @Inject constructor(
    private val menuItemsUseCases: MenuItemsUseCases,
    private val userUseCases: UserUseCases,
    private val application: Application
): AndroidViewModel(application) {

    var state by mutableStateOf(MenuItemsState())
        private set

    var currentUser by mutableStateOf(User())
        private set

    val isConnected: StateFlow<Boolean> = application.getConnectivityAsStateFlow(viewModelScope)

    fun onEvent(event: MenuItemsEvent) {
        when (event) {
            is MenuItemsEvent.OnLaunch -> {
                getData(event.restaurantId)
            }
        }
    }

    private fun getData(restaurantId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            state = state.copy(
                isLoading = true
            )
            currentUser = userUseCases.getUserObject()
            val menuItems = menuItemsUseCases.getRestaurantMenu(restaurantId)
            var showFallback = false
            if (menuItems.isEmpty() && !isConnected.value) {
                showFallback = true
            }
            state = state.copy(
                menuItems = menuItems,
                isLoading = false,
                showFallback = showFallback
            )
        }
    }

}