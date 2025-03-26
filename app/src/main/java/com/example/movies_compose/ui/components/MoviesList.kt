package com.example.movies_compose.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.example.movies_compose.R
import com.example.movies_compose.ui.models.MovieRV

/**
 * Componente que muestra una lista de películas en un grid adaptado al tamaño de la pantalla.
 *
 * Este componente se utiliza para mostrar películas en un grid, con soporte para mostrar películas favoritas, manejar la carga de datos, y navegar a los detalles de cada película.
 *
 * @param movies Los elementos de la lista de películas que se cargarán de manera paginada.
 * @param favorites Un conjunto que contiene los IDs de las películas marcadas como favoritas.
 * @param showNoFavorites Si es `true`, muestra un mensaje indicando que no hay películas favoritas si la lista está vacía.
 * @param isLoading Indica si las películas están cargando. Si es `true`, se muestra un componente de carga.
 * @param navigateToDetail Función de navegación que se invoca cuando se hace clic en una película, pasando el ID de la película.
 * @param onFavoriteClick Función que se invoca cuando se hace clic en el ícono de favorito de una película, permitiendo al usuario agregar o quitar la película de sus favoritos.
 *
 * Este componente utiliza una `LazyVerticalGrid` para mostrar las películas en un formato de grid, y adapta el número de columnas según la orientación de la pantalla. También maneja los estados de carga y muestra un mensaje cuando no hay elementos favoritos.
 *
 * @see LoadingComponent Componente que muestra un indicador de carga mientras se obtienen las películas.
 * @see NoFavorites Componente que muestra un mensaje si no hay películas favoritas.
 * @see MovieCard Componente que representa una tarjeta de película en el grid.
 */

@Composable
fun MoviesList(
    movies: LazyPagingItems<MovieRV>,
    favorites: Set<Int>,
    showNoFavorites: Boolean,
    isLoading: Boolean,
    navigateToDetail: (Int) -> Unit,
    onFavoriteClick: (MovieRV) -> Unit
){
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT

    val context = LocalContext.current
    val columns = context.resources.getInteger(
        if(isPortrait) R.integer.grid_column_count_v else R.integer.grid_column_count_h)

    if (isLoading) LoadingComponent()

    if (showNoFavorites && movies.itemCount == 0) {
        NoFavorites()
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxSize()
    ) {
        items(
            count = movies.itemCount,
            itemContent = { index: Int ->
                movies[index]?.let { movie ->
                    MovieCard(
                        movieRV = movie,
                        isFavorite = favorites.contains(movie.id),
                        onClick = {
                            navigateToDetail(movie.id)
                        },
                        onFavoriteClick = {
                            onFavoriteClick(movie)
                        }
                    )
                }
            }
        )
    }
}