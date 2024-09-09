package com.example.restau.presentation.navigation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.restau.R
import com.example.restau.presentation.navigation.items
import com.example.restau.ui.theme.Cream

@Composable
fun NavBar(
    onNav: (String) -> Unit,
    selected: Int,
    modifier: Modifier = Modifier
) {
    Column {
        HorizontalDivider(thickness = 1.dp, color = Color.Black)
        NavigationBar(
            modifier = modifier.fillMaxWidth(),
            containerColor = Color.White,
            tonalElevation = 5.dp
        ) {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    selected = selected == index,
                    onClick = { onNav(item.route) },
                    icon = {
                        Icon(
                            painter = painterResource(id = item.icon ?: R.drawable.error),
                            contentDescription = item.route,
                            tint = Color.Black
                        )
                    },
                    colors = NavigationBarItemDefaults.colors().copy(
                        selectedIndicatorColor = Cream
                    )
                )
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun NavBarPreview() {
    MaterialTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                NavBar(selected = 0, onNav = {})
            }
        ) {

        }
    }
}