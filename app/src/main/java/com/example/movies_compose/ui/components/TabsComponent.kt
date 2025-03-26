package com.example.movies_compose.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.movies_compose.R
import com.example.movies_compose.core.navigation.FavoriteMovies
import com.example.movies_compose.core.navigation.PopularMovies
import com.example.movies_compose.ui.models.NavigationItem
import com.example.movies_compose.ui.viewModels.MovieViewModel

@Composable
fun TabsComponent(navController: NavController, viewModel: MovieViewModel) {
    val selectedTabIndex = viewModel.selectedIndex.observeAsState()

    val context = LocalContext.current

    val navigationItems = listOf(
        NavigationItem(
            context.resources.getString(R.string.popular_navigation),
            Icons.Default.Star
        ),
        NavigationItem(
            context.resources.getString(R.string.favorites_navigation),
            Icons.Default.Favorite
        )
    )



    Column {
        TabRow(
            selectedTabIndex = selectedTabIndex.value ?: 0,
            containerColor = Color.White,
            divider = {}
        )
        {
            navigationItems.forEachIndexed { index, item ->
                Tab(
                    selected = selectedTabIndex.value == index,
                    onClick = {
                        viewModel.selectIndex(index)
                        navController.navigate(if (index == 0) PopularMovies else FavoriteMovies) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    },
                    text = { Text(text = item.title) },
                    icon = { Icon(imageVector = item.icon, contentDescription = item.title) }
                )
            }
        }
    }
}