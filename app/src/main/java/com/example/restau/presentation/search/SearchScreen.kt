package com.example.restau.presentation.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen() {
    var restaurantName by remember { mutableStateOf("") }
    val restaurantList = listOf("Restaurant 1", "Restaurant 2", "Restaurant 3") // Lista simulada

    Column(modifier = Modifier
        .padding(16.dp)
        .fillMaxSize()) {

        OutlinedTextField(
            value = restaurantName,
            onValueChange = { restaurantName = it },
            label = { Text("Restaurant Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(restaurantList.filter { it.contains(restaurantName, ignoreCase = true) }) { restaurant ->
                RestaurantItem(restaurantName = restaurant)
                Divider() // Separador entre items
            }
        }
    }
}

@Composable
fun RestaurantItem(restaurantName: String) {
    Text(
        text = restaurantName,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(8.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    SearchScreen() // Llama a tu pantalla principal para ver el preview
}