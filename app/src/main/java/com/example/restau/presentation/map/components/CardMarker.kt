package com.example.restau.presentation.map.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.withSave
import com.example.restau.domain.model.Restaurant
import com.example.restau.presentation.common.StarRating
import com.example.restau.ui.theme.Poppins


@Composable
fun CardMarker(
    restaurant: Restaurant,
    bitmap: ImageBitmap?,
    modifier: Modifier = Modifier
) {
    CustomShapeWithShadowContent(modifier) { innerModifier ->
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = innerModifier
                .fillMaxSize()
                .padding(horizontal = 10.dp)
        ) {
            Text(
                text = restaurant.name,
                fontSize = 20.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(horizontal = 17.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(13.dp))
            if (bitmap != null) {
                Image(
                    bitmap = bitmap,
                    contentDescription = restaurant.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(horizontal = 17.dp)
                        .clip(MaterialTheme.shapes.small)
                )
            } else {
                Box(
                    modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(horizontal = 17.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(Color.LightGray)
                )
            }
            Spacer(modifier = Modifier.height(13.dp))
            StarRating(value = restaurant.averageRating, size = 28.dp)
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = { /*TODO*/ },
                shape = MaterialTheme.shapes.extraLarge,
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = Color.White
                ),
                modifier = Modifier.padding(horizontal = 15.dp)
            ) {
                Text(
                    text = "Check Out",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Poppins,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun CustomShapeWithShadowContent(
    modifier: Modifier = Modifier,
    pointerWidth: Float = 90f,
    pointerHeight: Float = 135f,
    shadowRadius: Dp = 7.dp,
    content: @Composable (Modifier) -> Unit  // Content slot to pass in other composables
) {
    val density = LocalDensity.current

    Box(modifier = modifier
        .padding(7.dp)
        .width(235.dp)
        .height(360.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cornerRadius = 30.dp.toPx()
            val rectHeight = size.height - pointerHeight
            val rect = Rect(0f, 0f, size.width, rectHeight)

            val path = Path().apply {
                addRoundRect(
                    roundRect = RoundRect(
                        rect = rect,
                        cornerRadius = CornerRadius(cornerRadius, cornerRadius)
                    )
                )

                moveTo(size.width / 2 - pointerWidth, rectHeight)  // Left corner of the triangle
                lineTo(size.width / 2, size.height)               // Bottom tip of the triangle
                lineTo(size.width / 2 + pointerWidth, rectHeight)  // Right corner of the triangle
                close()
            }

            drawIntoCanvas { canvas ->
                with(density) {
                    canvas.nativeCanvas.withSave {
                        val paint = android.graphics.Paint().apply {
                            color = android.graphics.Color.GRAY
                            setShadowLayer(shadowRadius.toPx(), 0f, 4f, android.graphics.Color.argb(100, 0, 0, 0))
                        }
                        canvas.nativeCanvas.drawPath(path.asAndroidPath(), paint)
                    }
                }
            }

            drawPath(
                path = path,
                color = Color.White
            )
        }

        Box(modifier = Modifier
            .height(305.dp)
        ) {
            content(Modifier)
        }
    }
}
