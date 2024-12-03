package com.example.restau.presentation.restaurant

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.restau.R
import com.example.restau.presentation.common.DynamicTopBar
import com.example.restau.presentation.common.LoadingCircle
import com.example.restau.presentation.common.NoConnection
import com.example.restau.presentation.common.StarRating
import com.example.restau.presentation.common.TopBarAction
import com.example.restau.presentation.navigation.Route
import com.example.restau.ui.theme.Poppins
import com.example.restau.ui.theme.RestaUTheme


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RestaurantScreen(
    restaurantID: String?,
    restaurantViewModel: RestaurantViewModel = hiltViewModel(),
    navController: NavController,
    randomID: String? = ""
) {
    val isConnected by restaurantViewModel.isConnected.collectAsState()

    LaunchedEffect(isConnected) {
        restaurantViewModel.onEvent(RestaurantEvent.ScreenLaunched(restaurantID ?: ""))
    }

    LifecycleResumeEffect(Unit) {
        restaurantViewModel.onEvent(RestaurantEvent.ScreenOpened)

        onPauseOrDispose {
            restaurantViewModel.onEvent(RestaurantEvent.ScreenClosed)
        }
    }

    val context = LocalContext.current


    Scaffold(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize(),
        topBar = {
            DynamicTopBar(
                label = {},
                hasBackButton = true,
                action = TopBarAction.LocationAction {
                    if (restaurantViewModel.isConnected.value) restaurantViewModel.onEvent(RestaurantEvent.LaunchMaps(restaurantViewModel.state.restaurant.latitude, restaurantViewModel.state.restaurant.longitude, restaurantViewModel.state.restaurant.name, restaurantViewModel.state.restaurant.placeName,context))
                    else {
                        Toast.makeText(context, "No internet connection. Can't be redirected.", Toast.LENGTH_SHORT).show()
                    }},
                modifier = Modifier
                    .background(Color.White),
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    ) {
        if (!restaurantViewModel.showFallback){
            if (restaurantViewModel.state.restaurant.documentId.isNotEmpty()) {
                RestaurantContent(
                    restaurantVM = restaurantViewModel,
                    state = restaurantViewModel.state,
                    modifier = Modifier
                        .padding(
                            top = it.calculateTopPadding(),
                            start = it.calculateStartPadding(LayoutDirection.Ltr),
                            end = it.calculateEndPadding(LayoutDirection.Rtl)
                        )
                        .background(Color.White),
                    navController = navController,
                    randomID = randomID
                )
            } else LoadingCircle()
        } else {
            NoConnection()
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RestaurantContent(
    restaurantVM: RestaurantViewModel,
    state: RestaurantState,
    modifier: Modifier = Modifier,
    navController: NavController,
    randomID: String?
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
    ) {
        AsyncImage(
            model = state.restaurant.imageUrl,
            contentDescription = "${state.restaurant.name} Restaurant Image",
            contentScale = ContentScale.Crop,
            error = painterResource(id = R.drawable.restaurant),
            modifier = Modifier
                .height(300.dp)
        )

        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ){
            Text(
                text = state.restaurant.name,
                fontSize = 20.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(start = 30.dp, top = 30.dp)
            )

            if (state.match != null) {
                Text(
                    text = "${
                        if (state.match % 1 == 0.0f) state.match.toInt() else
                        state.match
                    }% match",
                    fontSize = 14.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(start = 10.dp, top = 30.dp),
                    color = if (state.match >= 30) Color(0xFF008615) else if (state.match >=15) Color(0xFFF4792C) else Color(0xFFB10000)
                )
            }
        }

        if (state.distance != null) {
            Text(
                text = "${
                    if (state.distance % 1 == 0.0) state.distance.toInt() else
                        state.distance
                }km - ${
                    if (state.distance > 3) "Far" else if (state.distance > 1) "Moderate" else "Near"
                }",
                fontSize = 14.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(start = 30.dp, top = 4.dp),
                color = if (state.distance > 3) Color(0xFFB10000) else if (state.distance > 1) Color(0xFFF4792C) else Color(0xFF008615)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )
        {
            Text(
                text = state.restaurant.averageRating.toString(),
                fontSize = 16.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Left,
                lineHeight = 16.sp,
                modifier = Modifier
                    .padding(start = 30.dp, end = 10.dp, bottom = 0.dp, top = 4.dp)
                    .wrapContentWidth()
                    .wrapContentHeight(),
                color = Color(0xFFA5A5A5)
            )

            StarRating(
                state.restaurant.averageRating,
                20.dp,
                showValue = false,
                horizontalArrangement = Arrangement.Start
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            ButtonOptionBar(
                icon = painterResource(id = R.drawable.group_331),
                name = "Menu",
                onClick = {
                    navController.navigate(Route.MenuListScreen.route + "/${state.restaurant.documentId}/${state.restaurant.name}")
                })
            ButtonOptionBar(
                icon = painterResource(id = R.drawable.calendar_month),
                name = "Schedule",
                onClick = { restaurantVM.onEvent(RestaurantEvent.ShowSchedule) })
            ButtonOptionBar(
                icon = painterResource(id = R.drawable.call),
                name = "Contact",
                onClick = {})
            ButtonOptionBar(
                icon = painterResource(id = R.drawable.kid_star),
                name = "Rate",
                onClick = {
                    navController.navigate(Route.ReviewListScreen.route  + "/${state.restaurant.documentId}" + "/${state.restaurant.name}" + "/$randomID")
                })
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            fontSize = 16.sp,
            fontFamily = Poppins,
            textAlign = TextAlign.Justify,
            color = Color.Gray,
            modifier = Modifier
                .padding(horizontal = 30.dp),
            text = state.restaurant.description
        )

        FlowRow(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            for (category in state.restaurant.categories) {
                CategoryTags(category)
            }
        }
    }

    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier.fillMaxSize()
    ) {
        LikeButton(
            isFavorite = restaurantVM.currentUser.likes.contains(state.restaurant.documentId),
            onFavorite = { restaurantVM.onEvent(RestaurantEvent.SendLike(state.restaurant.documentId)) },
            modifier = Modifier
                .padding(16.dp)
        )
    }

    if (state.showSchedule) SchedulePopup(
        state,
        onDismiss = { restaurantVM.onEvent(RestaurantEvent.ShowSchedule) })
}

@Composable
fun ButtonOptionBar(icon: Painter, name: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(10.dp, 5.dp, 10.dp, 0.dp),
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
fun CategoryTags(category: String) {
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .padding(5.dp, 5.dp, 10.dp, 5.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Text(
            text = category,
            color = Color(0xFF2F2F2F),
            fontFamily = Poppins,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 6.dp, horizontal = 12.dp)
        )
    }
}

@Composable
fun LikeButton(
    isFavorite: Boolean,
    onFavorite: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onFavorite,
        modifier = modifier
            .size(60.dp),
        colors = IconButtonDefaults.iconButtonColors().copy(
            containerColor = MaterialTheme.colorScheme.secondary
        )
    ) {
        Icon(
            painter = painterResource(
                id = if (isFavorite) R.drawable.filled_like else R.drawable.like
            ),
            contentDescription = "Like",
            tint = Color.White,
            modifier = Modifier.size(36.dp)
        )
    }
}

@Composable
fun SchedulePopup(
    state: RestaurantState = RestaurantState(),
    onDismiss: () -> Unit = {},
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.wrapContentSize(),
        text = {
            Box(modifier = Modifier.fillMaxWidth()) {
                Icon(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .clickable { onDismiss() },
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.wrapContentSize()
            ) {
                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = if (state.isOpen) "OPEN" else "CLOSED",
                    color = if (state.isOpen) Color(0xff008615) else Color(0xffB10000),
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    fontSize = 25.sp
                )

                Spacer(modifier = Modifier.height(10.dp))


                val schedule = state.restaurant.schedule

                val days = listOf(
                    "Sunday",
                    "Monday",
                    "Tuesday",
                    "Wednesday",
                    "Thursday",
                    "Friday",
                    "Saturday"
                )

                for (day in days) {
                    Row(
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = day,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.End,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            fontFamily = Poppins
                        )
                        Spacer(Modifier.width(15.dp))
                        Text(
                            text = "${formatTime((schedule[day.lowercase()]?.get("start") ?: 0).toInt())} - ${
                                formatTime(
                                    (schedule[day.lowercase()]?.get("end") ?: 0).toInt()
                                )
                            }",
                            fontSize = 15.sp,
                            fontFamily = Poppins
                        )
                    }
                }
            }
        },
        confirmButton = {
            Spacer(Modifier.size(0.dp))

        },
        dismissButton = {
            Spacer(Modifier.size(0.dp))
        },
        shape = RoundedCornerShape(16.dp),
    )
}

fun formatTime(inputTime: Int): String {
    val hours = inputTime / 100
    val minutes = inputTime % 100

    val period = if (hours >= 12) "pm" else "am"

    val formattedHours = when {
        hours == 0 -> 12
        hours > 12 -> hours - 12
        else -> hours
    }

    return "$formattedHours:${if (minutes == 0) "00" else minutes} $period"
}

@Preview
@Composable
fun RestaurantScreenPreview() {
    RestaUTheme {
        SchedulePopup()
    }
}