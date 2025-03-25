package com.example.movies_compose.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.movies_compose.data.api.RetrofitInstance
import com.example.movies_compose.data.bbdd.MovieDatabase
import com.example.movies_compose.data.repositories.MoviesRepository
import com.example.movies_compose.ui.components.MovieMainInformation
import com.example.movies_compose.ui.components.MovieOverview
import com.example.movies_compose.ui.viewModels.MovieViewModel
import com.example.movies_compose.ui.viewModels.MovieViewModelFactory

@Composable
fun MovieDetailScreen(navController: NavHostController, movieId: Int) {
    val moviesRepository = MoviesRepository(
        movieDAO = MovieDatabase.getDatabase(LocalContext.current).movieDao(),
        movieApiService = RetrofitInstance.movieApiService
    )

    val viewModel: MovieViewModel = viewModel(factory = MovieViewModelFactory(moviesRepository))
    viewModel.fetchMovieById(movieId)
    Log.d("MOVIE DETAIL SCREEN", "Id recibido: $movieId")

    val detailMovie by viewModel.movie.observeAsState()
    Log.d("MOVIE DETAIL SCREEN", "Detail movie recibido: $detailMovie")

    if(detailMovie != null) {
        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())) {
            MovieMainInformation(detailMovie!!)
            MovieOverview(detailMovie!!)
        }
    }  else {
        Log.e("MOVIE DETAIL SCREEN", "Error: movieId no recibido correctamente")
    }
}