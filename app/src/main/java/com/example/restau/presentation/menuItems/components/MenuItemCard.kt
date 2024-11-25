package com.example.restau.presentation.menuItems.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.restau.ui.theme.Poppins
import com.example.restau.ui.theme.RestaUTheme
import com.example.restau.ui.theme.SoftRed

@Composable
fun MenuItemCard(
    name: String,
    price: Int,
    imageUrl: String,
    onDetailClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
      modifier = modifier
          .padding(top = 4.dp)
          .fillMaxWidth()
          .height(174.dp)
          .shadow(
              elevation = 4.dp,
              shape = MaterialTheme.shapes.small,
              ambientColor = Color.Black.copy(alpha = 1f),
              spotColor = Color.Black.copy(alpha = 1f)
          )
          .background(Color.White)
          .clip(MaterialTheme.shapes.small)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 25.dp,
                    vertical = 18.dp
                )
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = name,
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .fillMaxHeight()
                    .width(129.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(Color.LightGray)
            )
            Spacer(modifier = Modifier.width(21.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = name,
                    color = Color(0xFF2F2F2F),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Poppins,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "$ $price",
                    color = Color.LightGray,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Poppins,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.weight(0.5f))
                Button(
                    onClick = { onDetailClick() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SoftRed,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .width(126.dp)
                        .height(41.dp)
                ) {
                    Text(
                        text = "Detail",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = Poppins,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(13.dp))
            }
        }
    }
}


@Preview
@Composable
fun MenuItemPreview() {
    RestaUTheme {
        MenuItemCard(
            name = "Doni's Waffles",
            price = 12000,
            imageUrl = "https://www.elespectador.com/resizer/gQOpefuhxVG5V8RYKBA9jZ0Rajc=/arc-anglerfish-arc2-prod-elespectador/public/2HSDHX52VNCN7EEQD6OUZNJDCE.jpg",
            onDetailClick = {}
        )
    }
}