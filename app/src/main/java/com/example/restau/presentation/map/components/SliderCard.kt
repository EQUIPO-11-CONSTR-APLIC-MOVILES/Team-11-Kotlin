package com.example.restau.presentation.map.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.restau.ui.theme.Poppins
import kotlin.math.roundToInt

@Composable
fun SliderCard(
    radius: Double,
    onChange: (Double) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors().copy(
            containerColor = Color.White
        ),
        modifier = modifier
            .padding(33.dp)
            .fillMaxWidth()
            .height(93.dp)
            .shadow(
                elevation = 3.dp,
                shape = RoundedCornerShape(25.dp),
                ambientColor = Color.Black.copy(alpha = 1f),
                spotColor = Color.Black.copy(alpha = 1f)
            )
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 22.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 27.dp)
            ) {
                Text(
                    text = "Search Range",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                )
                Text(
                    text = "${radius.roundToInt()} m",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(7.dp))
            CustomSlider(radius, onChange)
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun CustomSlider(radius: Double, onChange: (Double) -> Unit, modifier: Modifier = Modifier) {
    Slider(
        value = radius.toFloat(),
        onValueChange = {
            onChange(it.toDouble())
        },
        steps = 600,
        valueRange = 0f..600f,
        colors = SliderDefaults.colors().copy(
            thumbColor = MaterialTheme.colorScheme.secondary,
            activeTrackColor = MaterialTheme.colorScheme.primary,
            inactiveTrackColor = MaterialTheme.colorScheme.primary,
            activeTickColor = MaterialTheme.colorScheme.primary,
            inactiveTickColor = MaterialTheme.colorScheme.primary
        ),
        modifier = modifier
            .padding(horizontal = 27.dp)
            .fillMaxWidth(),
        thumb = {
            Spacer(
                modifier = Modifier
                    .size(16.dp)
                    .background(MaterialTheme.colorScheme.secondary, CircleShape)
            )
        },
        track = {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
            )
        }
    )
}