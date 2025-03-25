package com.example.movies_compose.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavController
import com.example.movies_compose.core.navigation.FavoriteMovies
import com.example.movies_compose.core.navigation.PopularMovies
import com.example.movies_compose.ui.models.NavigationItem

@Composable
fun BottomNavigationBar(navController: NavController) {
    val selectedNavigationIndex = rememberSaveable { mutableIntStateOf(0) }

    val navigationItems = listOf(
        NavigationItem("Popular", Icons.Default.Star),
        NavigationItem("Favorites", Icons.Default.Favorite)
    )

    NavigationBar{
        navigationItems.forEachIndexed { index, item ->
            val isSelected = selectedNavigationIndex.intValue == index
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    selectedNavigationIndex.intValue = index

                    when (index) {
                        0 -> {
                            navController.navigate(PopularMovies) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                            }
                        }
                        1 -> {
                            navController.navigate(FavoriteMovies) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                            }
                        }
                    }
                },
                icon = {
                    Icon(imageVector = item.icon, contentDescription = item.title)
                       },
                label = {
                    Text(
                        item.title,
                    )
                }
            )
        }
    }
}