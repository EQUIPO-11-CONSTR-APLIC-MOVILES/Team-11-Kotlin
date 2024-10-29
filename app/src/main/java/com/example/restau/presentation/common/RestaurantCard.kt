package com.example.restau.presentation.common

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.restau.R
import com.example.restau.ui.theme.Poppins
import com.example.restau.ui.theme.RestaUTheme

@Composable
fun RestaurantCard(
    isNew: Boolean,
    restaurantId: String,
    isFavorite: Boolean,
    name: String,
    imageUrl: String,
    placeName: String,
    averageRating: Float,
    onFavorite: () -> Unit,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    showLikeButton: Boolean = true,
    reload: Boolean = false
) {

    val imageRequest = ImageRequest.Builder(LocalContext.current).data(imageUrl).build()

    Box(
        modifier = modifier
            .height(320.dp)
            .shadow(
                elevation = 4.dp,
                shape = MaterialTheme.shapes.small,
                ambientColor = Color.Black.copy(alpha = 1f),
                spotColor = Color.Black.copy(alpha = 1f)
            )
            .clip(MaterialTheme.shapes.small)
            .background(Color.Gray)
            .clickable { onClick(restaurantId) }
    ) {
        if (reload) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "$name Restaurant Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                error = painterResource(id = R.drawable.restaurant)
            )
        } else {
            AsyncImage(
                model = imageRequest,
                contentDescription = "$name Restaurant Image reload",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                error = painterResource(id = R.drawable.restaurant)
            )
        }
        if (showLikeButton) {
            LikeButton(
                isFavorite,
                onFavorite,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 19.dp, top = 10.dp)
            )
        }
        if (isNew) {
            NewLabel(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 10.dp, end = 11.dp)
            )
        }
        RestaurantLabel(
            name, placeName, averageRating, modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 25.dp)
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
            .size(40.dp),
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
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun NewLabel(
    modifier: Modifier = Modifier,
) {
    Card(
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = Color.White,
        ),
        modifier = modifier
            .width(72.dp)
            .height(38.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "New",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = Poppins,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun RestaurantLabel(
    name: String,
    placeName: String,
    averageRating: Float,
    modifier: Modifier = Modifier,
) {
    Card(
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        modifier = modifier
            .width(224.dp)
            .height(75.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 17.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = name,
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )
                AverageRatingTag(averageRating)
            }
            PlaceNameText(placeName)
        }
    }
}

@Composable
fun AverageRatingTag(averageRating: Float, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(id = R.drawable.kid_star),
            contentDescription = "Star",
            tint = Color.Black,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = averageRating.toString(),
            color = Color.Black,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = Poppins
        )
    }
}

@Composable
private fun PlaceNameText(placeName: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.map),
            contentDescription = "Location Icon",
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = placeName,
            color = Color.Black,
            fontSize = 13.sp,
            fontFamily = Poppins,
            fontWeight = FontWeight.Light,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}


@Preview(showBackground = true)
@Composable
fun RestaurantCardPreview() {
    RestaUTheme {
        RestaurantCard(
            isNew = true,
            restaurantId = "12234",
            isFavorite = true,
            name = "Doni's",
            imageUrl = "https://www.allrecipes.com/thmb/lLeKelVvgs-yPAgqDfGrSzOOJIs=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/4431200-6c6df37091d341f3938dd5a4ee4b5f62.jpg",
            placeName = "Universidad de los Andes",
            averageRating = 4.8f,
            onFavorite = {},
            onClick = {restaurantId ->
                Log.d("TEST", restaurantId)
            }
        )
    }
}