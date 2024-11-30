package com.example.restau.domain.usecases.menuItemsUseCases

import com.example.restau.domain.model.MenuItem
import com.example.restau.domain.repository.MenuItemsRepository

class GetMenuItem(
    private val menuItemsRepository: MenuItemsRepository
) {
    suspend operator fun  invoke(itemId: String): MenuItem {
        return menuItemsRepository.getMenuItem(itemId)
    }
}