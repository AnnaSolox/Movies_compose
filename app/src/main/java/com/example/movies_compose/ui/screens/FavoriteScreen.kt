package com.example.movies_compose.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.movies_compose.data.api.RetrofitInstance
import com.example.movies_compose.data.bbdd.MovieDatabase
import com.example.movies_compose.data.repositories.MoviesRepository
import com.example.movies_compose.ui.components.LoadingComponent
import com.example.movies_compose.ui.components.MovieCard
import com.example.movies_compose.ui.components.NoFavorites
import com.example.movies_compose.ui.models.Routes
import com.example.movies_compose.ui.viewModels.MovieViewModel
import com.example.movies_compose.ui.viewModels.MovieViewModelFactory

@Composable
fun FavoriteScreen(navigateToDetail: (Int) -> Unit) {
    val moviesRepository = MoviesRepository(
        movieDAO = MovieDatabase.getDatabase(LocalContext.current).movieDao(),
        movieApiService = RetrofitInstance.movieApiService
    )

    val viewModel: MovieViewModel = viewModel(factory = MovieViewModelFactory(moviesRepository))

    val favoriteMovies = viewModel.favorites.collectAsLazyPagingItems()

    if (favoriteMovies.loadState.refresh is LoadState.Loading) {
        LoadingComponent()
    }

    if (favoriteMovies.itemCount == 0){
        NoFavorites()
    }

    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 8.dp)
    ) {
        items(
            count = favoriteMovies.itemCount,
            key = { index -> favoriteMovies[index]?.id ?: index  },
            itemContent = { index: Int ->
                favoriteMovies[index]?.let {
                    movie ->
                    MovieCard(
                        movieRV = movie,
                        isFavorite = true,
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