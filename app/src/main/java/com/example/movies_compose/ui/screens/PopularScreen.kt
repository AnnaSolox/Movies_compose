package com.example.movies_compose.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.movies_compose.data.api.RetrofitInstance
import com.example.movies_compose.data.bbdd.MovieDatabase
import com.example.movies_compose.data.repositories.MoviesRepository
import com.example.movies_compose.ui.components.MovieCard
import com.example.movies_compose.ui.models.Routes
import com.example.movies_compose.ui.viewModels.MovieViewModel
import com.example.movies_compose.ui.viewModels.MovieViewModelFactory

@Composable
fun PopularScreen(navController: NavHostController) {
    val moviesRepository = MoviesRepository(
        movieDAO = MovieDatabase.getDatabase(LocalContext.current).movieDao(),
        movieApiService = RetrofitInstance.movieApiService
    )

    val viewModel: MovieViewModel = viewModel(factory = MovieViewModelFactory(moviesRepository))

    val popularMovies = viewModel.movies.collectAsLazyPagingItems()
    val favoriteMovieIds by viewModel.favoriteMovieIds.collectAsState(initial = emptySet())

    LazyColumn(Modifier.padding(horizontal = 8.dp)) {
        items(
            count = popularMovies.itemCount,
            key = { index -> popularMovies[index]?.id ?: index },
            itemContent = { index: Int ->
                popularMovies[index]?.let { movie ->
                    MovieCard(
                        movieRV = movie,
                        isFavorite = favoriteMovieIds.contains(movie.id),
                        onClick = {
                            navController.navigate(Routes.DetailScreen.createRoute(movie.id))},
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