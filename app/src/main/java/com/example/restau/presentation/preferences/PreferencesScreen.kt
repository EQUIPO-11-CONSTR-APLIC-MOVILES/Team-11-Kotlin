package com.example.restau.presentation.preferences

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.restau.presentation.navigation.Route
import com.example.restau.ui.theme.Poppins

@Composable
fun PreferencesScreen(
    preferencesVM: PreferencesViewModel = hiltViewModel(),
    navController: NavHostController,
    authCheck: suspend () -> Unit
) {
    val tags = mapOf(
        "Cuisine" to listOf("Italian", "Chinese", "Indian", "Japanese", "Mexican", "Thai", "Vietnamese"),
        "Dish Type" to listOf("Pizza", "Burger", "Pasta", "Sushi", "Salad", "Sandwich", "Soup"),
        "Meal Type" to listOf("Breakfast", "Brunch", "Lunch", "Dinner", "Dessert", "Snack"),
        "Dietary" to listOf("Vegetarian", "Vegan", "Gluten Free", "Dairy Free", "Nut Free", "Halal", "Kosher"),
        "Price Range" to listOf("Budget Friendly", "Mid Range", "Fine Dining"),
        "Dining Style" to listOf("Casual", "Fast Food", "Food Truck", "Cafe", "Bar", "Pub", "Bakery"),
        "Atmosphere" to listOf("Family Friendly", "Romantic", "Outdoor", "Pet Friendly", "Quiet", "Lively"),
        "Features" to listOf("Takeout", "Delivery", "Reservations", "Outdoor Seating", "Wifi", "TV", "Live Music")
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Tell us what\n you like!",
                color = Color(0xFF2F2F2F),
                fontFamily = Poppins,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 90.dp, 0.dp, 20.dp)
            )

            preferencesVM.onEvent(PreferencesEvent.VerticalScroll(rememberScrollState()))

            preferencesVM.state.verticalScrollState?.let { Modifier.verticalScroll(it) }?.let {
                Column(
                    modifier = it,
                ) {

                    for ((index, tagGroup) in tags.entries.withIndex()) {
                        TagGroup(
                            preferencesVM = preferencesVM,
                            index = index,
                            tagName = tagGroup.key,
                            tagList = tagGroup.value
                        )
                    }
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
        Button(
            onClick = {
                preferencesVM.onEvent(PreferencesEvent.SaveTags(preferencesVM.state.selectedTags, { savedSuccess(navController) }, {  authCheck() }) )
            },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
            ),
            enabled = preferencesVM.state.selectedTags.isNotEmpty()
        ) {
            Text(
                text = "Let's go!",
                color = Color.White,
                fontFamily = Poppins,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(10.dp)
            )
        }
    }
}

@Composable
fun TagGroup(
    preferencesVM: PreferencesViewModel = hiltViewModel(),
    index: Int,
    tagName: String,
    tagList: List<String>
) {
    Column(
        modifier = Modifier
            .padding(0.dp, 5.dp, 0.dp, 10.dp)
    ) {
        preferencesVM.onEvent(PreferencesEvent.StartScroll(index, rememberScrollState()))
        Text(
            text = tagName,
            color = Color(0xFF2F2F2F),
            fontFamily = Poppins,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(40.dp, 10.dp, 10.dp, 10.dp)
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(preferencesVM.state.scrollStates[index]),

            ) {
            Spacer(modifier = Modifier.width(40.dp))
            for (tag in tagList) {
                var color = Color.White
                if (tag in preferencesVM.state.selectedTags) color =
                    MaterialTheme.colorScheme.primary
                ElevatedCard(
                    colors = CardDefaults.elevatedCardColors(containerColor = color),
                    modifier = Modifier
                        .padding(5.dp, 0.dp, 10.dp, 0.dp)
                        .selectable(
                            selected = tag in preferencesVM.state.selectedTags,
                            onClick = {
                                preferencesVM.onEvent(PreferencesEvent.SelectTag(tag))
                            },
                        ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    )

                ) {
                    Text(
                        text = tag,
                        color = Color(0xFF2F2F2F),
                        fontFamily = Poppins,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(10.dp, 7.dp, 10.dp, 7.dp)
                    )
                }

            }

        }
    }
}

fun savedSuccess(navController: NavController) {
    navController.navigate(Route.HomeScreen.route) {
        popUpTo(navController.graph.startDestinationId){
            inclusive = true
        }
    }
}