package com.example.restau.presentation.common

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.restau.R
import com.example.restau.ui.theme.RestaUTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DynamicTopBar(
    label: @Composable () -> Unit,
    hasBackButton: Boolean,
    action: TopBarAction,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit) = {},
) {

    Column(
        modifier = modifier
    ) {
        CenterAlignedTopAppBar(
            title = label,
            navigationIcon = {
                BackButton(hasBackButton, onBack)
            },
            actions = {
                ActionButton(action, modifier)
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors().copy(
                containerColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .padding(horizontal = 15.dp, vertical = 12.dp)
        )
        HorizontalDivider(thickness = 1.dp, color = Color.Black)
    }
}

@Composable
private fun ActionButton(
    action: TopBarAction,
    modifier: Modifier,
) {
    Box(
        modifier = modifier.fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        when (action) {
            is TopBarAction.PhotoAction -> {
                AsyncImage(
                    model = action.imageUrl,
                    contentDescription = "User Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(48.dp)
                        .background(action.color)
                        .clip(MaterialTheme.shapes.extraLarge)
                        .clickable {
                            action.onPhoto()
                        }
                )
            }

            is TopBarAction.LocationAction -> {
                IconButton(
                    onClick = { action.onLocation() },
                    modifier = Modifier
                        .size(48.dp),
                    colors = IconButtonDefaults.iconButtonColors().copy(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.map_filled),
                        contentDescription = "Directions",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun BackButton(
    hasBackButton: Boolean,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        if (hasBackButton) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Arrow Back",
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun DynamicTopBarPreview() {
    RestaUTheme(darkTheme = false) {
        Scaffold(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize(),
            topBar = {
                DynamicTopBar(
                    label = {},
                    hasBackButton = true,
                    action = TopBarAction.LocationAction {}
                )
            }
        ) {

        }
    }
}