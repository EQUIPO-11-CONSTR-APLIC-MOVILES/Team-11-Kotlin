package com.example.restau.presentation.common

import androidx.compose.ui.graphics.Color
import com.example.restau.ui.theme.SoftRed

sealed class TopBarAction(
    val color: Color
) {

    data class PhotoAction(val imageUrl: String, val onPhoto: (() -> Unit)): TopBarAction(color = Color.Transparent)

    data class LocationAction(val onLocation: (() -> Unit)): TopBarAction(color = SoftRed)

    data object NoAction: TopBarAction(color = SoftRed)

}