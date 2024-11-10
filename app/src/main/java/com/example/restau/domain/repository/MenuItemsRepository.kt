package com.example.restau.domain.repository

import com.example.restau.domain.model.MenuItem

interface MenuItemsRepository {

    suspend fun getMenuItems(restaurantId: String): List<MenuItem>

}