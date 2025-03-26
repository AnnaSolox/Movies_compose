package com.example.movies_compose.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.movies_compose.R

/**
 * Composable que muestra una barra superior (TopAppBar) con un título y un icono de navegación.
 * El icono de navegación solo se muestra si el título actual no es uno de los títulos excluidos, permitiendo al usuario volver a la pantalla anterior en la navegación.
 *
 * @param navController El controlador de navegación que se utiliza para navegar entre las pantallas.
 * @param currentTitle El título actual que se mostrará en la barra superior.
 *
 * @see TopAppBar Componente que representa la barra superior.
 * @see IconButton Componente que maneja el clic en el icono de navegación.
 * @see Icon Muestra el icono de flecha hacia atrás para volver a la pantalla anterior.
 * @see Text Muestra el título actual en la barra superior.
 *
 * Esta función personaliza la barra superior para que, si el título no es uno de los excluidos, aparezca un icono de flecha hacia atrás que permite navegar a la pantalla anterior.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavController, currentTitle: String) {
    val context = LocalContext.current
    val excludedTitlesIcon =
        listOf(
            context.resources.getString(R.string.popular_topbar_title),
            context.resources.getString(R.string.favorites_topbar_title),
        )
    TopAppBar(
        title = { Text(text = currentTitle) },
        navigationIcon = {
            if (currentTitle !in excludedTitlesIcon) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = context.resources.getString(R.string.icon_arrowback_topbar)
                    )
                }
            }
        }
    )
}