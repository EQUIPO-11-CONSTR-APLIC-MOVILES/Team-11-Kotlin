package com.example.restau.presentation.menuItems

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.restau.R
import com.example.restau.domain.model.MenuItem
import com.example.restau.presentation.common.DynamicTopBar
import com.example.restau.presentation.common.LoadingCircle
import com.example.restau.presentation.common.NoConnection
import com.example.restau.presentation.common.TopBarAction
import com.example.restau.presentation.menuItems.components.MenuItemCard
import com.example.restau.ui.theme.Poppins

@Composable
fun MenuItemsScreen(
    restaurantId: String,
    restaurantName: String,
    navController: NavController,
    menuItemsViewModel: MenuItemsViewModel = hiltViewModel()
) {

    val isConnected by menuItemsViewModel.isConnected.collectAsState()

    LaunchedEffect(isConnected) {
        menuItemsViewModel.onEvent(MenuItemsEvent.OnLaunch(restaurantId))
    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            DynamicTopBar(
                label = { },
                hasBackButton = true,
                action = TopBarAction.PhotoAction(
                    imageUrl = menuItemsViewModel.currentUser.profilePic,
                    onPhoto = {}
                ),
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    ) {
        if (!menuItemsViewModel.state.isLoading && !menuItemsViewModel.state.showFallback) {
            MenuItemsContent(
                restaurantName = restaurantName,
                items = menuItemsViewModel.state.menuItems,
                modifier = Modifier.padding(
                    top = it.calculateTopPadding(),
                    start = it.calculateLeftPadding(LayoutDirection.Ltr),
                    end = it.calculateRightPadding(LayoutDirection.Rtl),
                )
            )
        } else if (menuItemsViewModel.state.isLoading) {
            LoadingCircle()
        } else if (menuItemsViewModel.state.showFallback) {
            NoConnection()
        }
    }
}

@Composable
fun MenuItemsContent(
    restaurantName: String,
    items: List<MenuItem>,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(painter = painterResource(id = R.drawable.group_331), contentDescription = "menu")
            Spacer(modifier = Modifier.width(9.dp))
            Text(
                text = restaurantName,
                color = Color(0xFF2F2F2F),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = Poppins,
                textAlign = TextAlign.Center,
            )

        }
        Spacer(modifier = Modifier.height(21.dp))
        MenuItemsLazyList(
            items = items
        )
    }
}

@Composable
fun MenuItemsLazyList(
    items: List<MenuItem>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = 30.dp)
    ) {
        items(items) {
            MenuItemCard(
                name = it.name,
                price = it.price,
                imageUrl = it.imageUrl,
                onDetailClick = { /*TODO*/ }
            )
            Spacer(modifier = Modifier.height(25.dp))
        }
    }
}
