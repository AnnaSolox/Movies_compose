package com.example.movies_compose.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.movies_compose.R

/**
 * Composable que muestra una interfaz de usuario cuando no hay películas favoritas. Muestra un icono de película y un texto indicando que no se han seleccionado películas como favoritas.
 *
 * @see Icon Muestra un icono representando una película.
 * @see Text Muestra un texto que indica que no hay películas favoritas.
 * @see Column Organiza los elementos de la pantalla en una columna, centrando el contenido.
 * @see Modifier Ajustes de tamaño y alineación de los elementos.
 */
@Preview(showBackground = true)
@Composable
fun NoFavorites() {
    val context = LocalContext.current

    Column(Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painterResource(id = R.drawable.baseline_movie_24),
            contentDescription = context.resources.getString(R.string.movies_icon),
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Text(text = context.resources.getString(R.string.no_favorite_movies), style = MaterialTheme.typography.bodyMedium)
    }
}