package com.example.restau.presentation.restaurant

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.restau.R
import com.example.restau.presentation.common.DynamicTopBar
import com.example.restau.presentation.common.TopBarAction
import com.example.restau.presentation.preferences.PreferencesEvent
import com.example.restau.ui.theme.Poppins
import com.example.restau.ui.theme.RestaUTheme


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RestaurantScreen() {
    Scaffold(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize(),
        topBar = {
            DynamicTopBar(
                label = {},
                hasBackButton = true,
                action = TopBarAction.LocationAction({/*TODO*/}),
                modifier = Modifier
                    .background(Color.White)
            )
        }
    ) {
        RestaurantContent(
            modifier = Modifier.padding(
                top = it.calculateTopPadding(),
                start = it.calculateStartPadding(LayoutDirection.Ltr),
                end = it.calculateEndPadding(LayoutDirection.Rtl)
            )
                .background(Color.White)
        )
    }
}

@Composable
fun RestaurantContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Text(
            text = "Restaurant Name",
            fontSize = 20.sp,
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(start = 16.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        )
        {
            Text(
                text = "4.8",
                fontSize = 18.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .padding(start = 16.dp, end = 10.dp),
                color = Color(0xFFA5A5A5)
            )

            Stars(3.8f)
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            ButtonOptionBar(icon = painterResource(id = R.drawable.group_331), name = "Menu", onClick = {})
            ButtonOptionBar(icon = painterResource(id = R.drawable.calendar_month), name = "Schedule", onClick = {})
            ButtonOptionBar(icon = painterResource(id = R.drawable.call), name = "Contact", onClick = {})
            ButtonOptionBar(icon = painterResource(id = R.drawable.kid_star), name = "Rate", onClick = {})
        }
    }
}

@Composable
fun Stars(rating: Float){
    Row {
        for (i in 1..5) {
            if (i <= rating) {
                Icon(
                    painter = painterResource(id = R.drawable.filledstar),
                    contentDescription = "Star",
                    tint = Color(0xFFFFEEAD),
                    modifier = Modifier
                        .padding(horizontal = 2.dp)
                        .size(25.dp)
                )
            } else if((i-0.5) <= rating) {
                Icon(
                    painter = painterResource(id = R.drawable.halffilledstar),
                    contentDescription = "Star",
                    tint = Color(0xFFFFEEAD),
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(25.dp)
                )
            }
            else {
                Icon(
                    painter = painterResource(id = R.drawable.outlinedstar),
                    contentDescription = "Star",
                    tint = Color(0xFFFFEEAD),
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(25.dp)
                )
            }
        }
    }
}

@Composable
fun ButtonOptionBar(icon: Painter, name: String, onClick: () -> Unit){
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = icon,
            contentDescription = "$name Icon",
            modifier = Modifier.size(28.dp)
        )

        Text(
            text = name,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp,
            fontFamily = Poppins,
            color = Color.Black
        )
    }
}


@Composable
fun CategoryTags(category: String){
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        modifier = Modifier
            .padding(5.dp, 0.dp, 10.dp, 0.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Text(
            text = category,
            color = Color(0xFF2F2F2F),
            fontFamily = Poppins,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(10.dp, 7.dp, 10.dp, 7.dp)
        )
    }

}


@Preview
@Composable
fun RestaurantScreenPreview() {
    RestaUTheme {
        RestaurantScreen()
    }
}