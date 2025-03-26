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

/**
 * Composable que muestra un conjunto de pestañas de navegación para cambiar entre las diferentes vistas de películas populares y favoritas. Utiliza un `TabRow` para mostrar las pestañas y manejar la selección y navegación a las pantallas correspondientes.
 *
 * @param navController El controlador de navegación que se utiliza para navegar entre las pantallas.
 * @param viewModel El `MovieViewModel` que gestiona el estado de la aplicación y la pestaña seleccionada.
 *
 * @see TabRow Contenedor para las pestañas que permite la selección.
 * @see Tab Representa una pestaña que, al ser seleccionada, navega a la vista correspondiente.
 * @see Column Organiza las pestañas en una columna.
 * @see Text Muestra el título de cada pestaña.
 * @see Icon Muestra un icono en cada pestaña.
 *
 * Esta función muestra las pestañas "Popular" y "Favorites", permitiendo al usuario navegar entre ellas y actualizar el contenido visible en función de la pestaña seleccionada.
 * El índice seleccionado se gestiona desde el viewModel.
 */
@Composable
fun TabsComponent(navController: NavController, viewModel: MovieViewModel) {
    val selectedTabIndex = viewModel.selectedIndex.observeAsState()

    val context = LocalContext.current

    val navigationItems = listOf(
        NavigationItem(
            context.resources.getString(R.string.popular_bottom_menu),
            Icons.Default.Star
        ),
        NavigationItem(
            context.resources.getString(R.string.favorites_bottom_menu),
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