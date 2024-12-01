package com.example.restau.presentation.menuDetail

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.restau.R
import com.example.restau.presentation.common.DynamicTopBar
import com.example.restau.presentation.common.LoadingCircle
import com.example.restau.presentation.common.NoConnection
import com.example.restau.presentation.common.TopBarAction
import com.example.restau.ui.theme.Poppins

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MenuDetailScreen(
    itemID: String?,
    restaurantName: String?,
    viewModel: MenuDetailViewModel = hiltViewModel(),
    navController: NavHostController
) {

    val isConnected by viewModel.isConnected.collectAsState()

    LaunchedEffect(isConnected) {
        viewModel.onEvent(MenuDetailEvent.OnLaunch(itemID ?: ""))
    }

    Scaffold(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize(),
        topBar = {
            DynamicTopBar(
                label = {},
                hasBackButton = true,
                action = TopBarAction.NoAction,
                modifier = Modifier
                    .background(Color.White),
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    ) {
        if (!viewModel.showFallback) {
            if (viewModel.state.item.documentId.isNotEmpty()) {
                Content(
                    viewModel = viewModel,
                    restaurantName = restaurantName ?: "",
                    modifier = Modifier.padding(
                        top = it.calculateTopPadding(),
                        start = it.calculateLeftPadding(LayoutDirection.Ltr),
                        end = it.calculateRightPadding(LayoutDirection.Rtl),
                    ),
                )
            } else {
                LoadingCircle()
            }
        } else {
            NoConnection()
        }

    }
}


@Composable
fun Content(
    viewModel: MenuDetailViewModel,
    restaurantName: String,
    modifier: Modifier = Modifier
){
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

        AsyncImage(
            model = viewModel.state.item.imageUrl,
            contentDescription = "${viewModel.state.item.name} Item Image",
            contentScale = ContentScale.Crop,
            error = painterResource(id = R.drawable.restaurant),
            modifier = Modifier
                .height(300.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(Color.LightGray)
                .width(LocalConfiguration.current.screenWidthDp.dp - 60.dp)
        )

        Spacer(modifier = Modifier.height(25.dp))

        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
        )
        {
            Text(
                text = viewModel.state.item.name,
                fontSize = 20.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .weight(0.7f)
            )

            Text(
                text = "$ ${viewModel.state.item.price}",
                fontSize = 18.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF008615),
                textAlign = TextAlign.Right,
                modifier = Modifier
                    .weight(0.3f)
            )

        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            fontSize = 16.sp,
            fontFamily = Poppins,
            textAlign = TextAlign.Justify,
            color = Color.Gray,
            modifier = Modifier
                .padding(horizontal = 30.dp),
            text = viewModel.state.item.description
        )
    }
}
//
//@Preview
//@Composable
//fun RestaurantScreenPreview() {
//    RestaUTheme {
//        MenuDetailScreen()
//    }
//}