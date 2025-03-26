package com.example.movies_compose.ui.models

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Modelo que representa los items de la navegación de los Tabs
 */
data class NavigationItem(
    val title: String,
    val icon: ImageVector,
)
