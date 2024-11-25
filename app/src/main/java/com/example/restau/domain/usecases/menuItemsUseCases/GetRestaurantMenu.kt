package com.example.restau.domain.usecases.menuItemsUseCases

import com.example.restau.domain.model.MenuItem
import com.example.restau.domain.repository.MenuItemsRepository

class GetRestaurantMenu(
    private val menuItemsRepository: MenuItemsRepository
) {

    suspend operator fun  invoke(restaurantId: String): List<MenuItem> {
        return menuItemsRepository.getMenuItems(restaurantId)
    }

}