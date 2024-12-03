package com.example.restau.presentation.menuItems

import com.example.restau.domain.model.MenuItem

data class MenuItemsState(
    val menuItems: List<MenuItem> = emptyList(),
    val isLoading: Boolean = true,
    val showFallback: Boolean = false
)