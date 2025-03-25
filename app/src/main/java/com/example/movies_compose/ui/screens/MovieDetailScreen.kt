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
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movies_compose.data.api.RetrofitInstance
import com.example.movies_compose.data.bbdd.MovieDatabase
import com.example.movies_compose.data.repositories.MoviesRepository
import com.example.movies_compose.ui.components.LoadingComponent
import com.example.movies_compose.ui.components.MovieMainInformation
import com.example.movies_compose.ui.components.MovieOverview
import com.example.movies_compose.ui.viewModels.MovieViewModel
import com.example.movies_compose.ui.viewModels.MovieViewModelFactory

@Composable
fun MovieDetailScreen( movieId: Int) {
    val moviesRepository = MoviesRepository(
        movieDAO = MovieDatabase.getDatabase(LocalContext.current).movieDao(),
        movieApiService = RetrofitInstance.movieApiService
    )

    val viewModel: MovieViewModel = viewModel(factory = MovieViewModelFactory(moviesRepository))
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