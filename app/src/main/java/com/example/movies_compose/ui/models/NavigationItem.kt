package com.example.movies_compose.ui.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

val navigationItems = listOf(
    NavigationItem("Popular", Icons.Default.Star, Routes.PopularScreen.route),
    NavigationItem("Favorites", Icons.Default.Favorite, Routes.FavoritesScreen.route)
)
