package com.example.movies_compose.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.example.movies_compose.ui.components.LoadingComponent
import com.example.movies_compose.ui.components.MovieMainInformation
import com.example.movies_compose.ui.components.MovieOverview
import com.example.movies_compose.ui.viewModels.MovieViewModel

/**
 * Composable que muestra la pantalla de detalles de una película.
 * La pantalla obtiene los detalles de la película utilizando su ID, y muestra la información principal de la película, así como su descripción.
 * También permite agregar o quitar la película de la lista de favoritos.
 *
 * @param movieId El ID de la película cuya información se debe cargar y mostrar en la pantalla.
 * @param viewModel El ViewModel que maneja la lógica de obtención de datos y la actualización de favoritos.
 *
 * La función realiza una solicitud para obtener los detalles de la película a través de `viewModel.fetchMovieById`.
 * Si la película está cargando, se muestra un indicador de carga. Si los detalles de la película se han obtenido correctamente, se muestran en la pantalla. Además, el usuario puede marcar o desmarcar la película como favorita, lo que actualiza la base de datos utilizando la función `viewModel.updateFavorites`.
 *
 * @see MovieMainInformation Componente que muestra la información principal de la película, incluyendo título, fecha de estreno, y voto.
 * @see MovieOverview Componente que muestra la descripción de la película.
 * @see LoadingComponent Componente que muestra un indicador de carga mientras los detalles de la película están siendo obtenidos.
 *
 * @throws Log.d y Log.e se utilizan para depuración, registrando el estado de la aplicación en la consola.
 */
@Composable
fun MovieDetailScreen( movieId: Int, viewModel: MovieViewModel) {

    viewModel.fetchMovieById(movieId)
    Log.d("MOVIE DETAIL SCREEN", "Id recibido: $movieId")

    val detailMovie by viewModel.movie.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState()

    val favoriteMovieIds by viewModel.favoriteMovieIds.collectAsState(initial = emptySet())

    Log.d("MOVIE DETAIL SCREEN", "Detail movie recibido: $detailMovie")

    if (isLoading == true){
        LoadingComponent()
    }

    if(detailMovie != null) {
        Column(modifier = Modifier
            .verticalScroll(rememberScrollState())) {
            MovieMainInformation(detailMovie!!,
                isFavorite = favoriteMovieIds.contains(detailMovie!!.id) ,
                onFavoriteClick = {
                    viewModel.getMovieByIdFromDb(detailMovie!!.id) { movieDB ->
                        movieDB?.let {
                            movieDB.title?.let { viewModel.setCurrentScreenTitle(movieDB.title) }
                            it.isFavourite = !it.isFavourite
                            viewModel.updateFavorites(it)
                        }
                    }
                })
            MovieOverview(detailMovie!!)
        }
    }  else {
        Log.e("MOVIE DETAIL SCREEN", "Error: movieId no recibido correctamente")
    }
}