package com.example.movies_compose.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.movies_compose.R
import com.example.movies_compose.ui.components.LoadingComponent
import com.example.movies_compose.ui.components.MovieCard
import com.example.movies_compose.ui.components.NoFavorites
import com.example.movies_compose.ui.viewModels.MovieViewModel

/**
 * Composable que muestra la pantalla de películas populares.
 * La pantalla muestra una lista de películas populares en un grid adaptado al tipo de orientación (vertical u horizontal) de la pantalla.
 * Si no hay películas populares, muestra un mensaje indicándolo.
 * Si las películas están cargando, muestra un componente de carga.
 *
 * @param navigateToDetail Función de navegación que se invoca al hacer clic en una película para mostrar sus detalles.
 * @param viewModel El ViewModel que contiene la lógica y los datos de las películas populares, y maneja las operaciones relacionadas con la base de datos y la actualización de favoritos.
 *
 * @see MovieCard Componente que representa una tarjeta de película que se muestra en la grid.
 * @see LoadingComponent Componente que muestra un indicador de carga mientras se obtienen las películas.
 * @see NoFavorites Componente que muestra un mensaje si no hay películas populares.
 * @see LazyVerticalGrid Componente que permite la visualización de un grid con columnas adaptativas según la orientación.
 * @see GridCells.Fixed Establece el número de columnas en el grid según la configuración del dispositivo.
 *
 * En esta función, las películas populares se cargan de manera paginada y se muestran en una grid.
 * Se gestiona el estado de carga y la visualización de mensajes cuando no hay elementos en la lista.
 * Además, se permite al usuario actualizar el estado de favorito de cada película.
 */
@Composable
fun PopularScreen(navigateToDetail: (Int) -> Unit, viewModel: MovieViewModel) {

    val popularMovies = viewModel.movies.collectAsLazyPagingItems()
    val favoriteMovieIds by viewModel.favoriteMovieIds.collectAsState(initial = emptySet())

    if (popularMovies.loadState.refresh is LoadState.Loading) {
        LoadingComponent()
    }

    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT

    val context = LocalContext.current
    val columns = context.resources.getInteger(
        if(isPortrait) R.integer.grid_column_count_v else R.integer.grid_column_count_h)



    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxSize()
    ) {
        items(
            count = popularMovies.itemCount,
            itemContent = { index: Int ->
                popularMovies[index]?.let { movie ->
                    MovieCard(
                        movieRV = movie,
                        isFavorite = favoriteMovieIds.contains(movie.id),
                        onClick = {
                            navigateToDetail(movie.id)
                        },
                        onFavoriteClick = {
                            movie.isFavorite = !movie.isFavorite

                            viewModel.getMovieByIdFromDb(movie.id) { movieDB ->
                                movieDB?.let {
                                    it.isFavourite = movie.isFavorite
                                    viewModel.updateFavorites(it)
                                }
                            }
                        }
                    )
                }
            }
        )
    }
}
