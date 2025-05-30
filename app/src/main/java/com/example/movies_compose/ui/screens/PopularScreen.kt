package com.example.movies_compose.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.movies_compose.ui.components.MoviesList
import com.example.movies_compose.ui.viewModels.MovieViewModel

/**
 * Pantalla que muestra las películas populares.
 *
 * Esta pantalla se utiliza para mostrar una lista de películas populares obtenidas del `viewModel`.
 * Utiliza un `MoviesList` para renderizar las películas y manejar la lógica de favoritos, como mostrar las películas, agregar o quitar de los favoritos y navegar a la pantalla de detalles de una película.
 *
 * @param navigateToDetail Función de navegación que se invoca cuando el usuario selecciona una película, pasando el ID de la película para mostrar los detalles.
 * @param viewModel El `MovieViewModel` que proporciona los datos relacionados con las películas populares y maneja la lógica de negocio, como obtener las películas desde la base de datos o actualizar el estado de favorito de una película.
 *
 * La lista de películas populares se obtiene del `viewModel` a través de un `LazyPagingItems`, y se gestiona el estado de carga con `LoadState`. Además, se puede modificar el estado de favorito de cada película y actualizarlo en la base de datos.
 *
 * @see MoviesList Componente que se encarga de mostrar las películas en un grid.
 */

@Composable
fun PopularScreen(navigateToDetail: (Int) -> Unit, viewModel: MovieViewModel) {

    val popularMovies = viewModel.movies.collectAsLazyPagingItems()
    val favoriteMovieIds by viewModel.favoriteMovieIds.collectAsState(initial = emptySet())

    val isLoading = popularMovies.loadState.refresh is LoadState.Loading

    val errorState = popularMovies.loadState.refresh as? LoadState.Error
        ?: popularMovies.loadState.append as? LoadState.Error

    errorState?.let { error ->
        androidx.compose.runtime.LaunchedEffect(error) {
            android.util.Log.e("PopularScreen", "Error cargando películas: ${error.error.localizedMessage}")
        }
    }

    MoviesList(
        movies = popularMovies,
        favorites = favoriteMovieIds,
        isLoading = isLoading,
        showNoFavorites = false,
        navigateToDetail = navigateToDetail,
        onFavoriteClick = { movie ->
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
