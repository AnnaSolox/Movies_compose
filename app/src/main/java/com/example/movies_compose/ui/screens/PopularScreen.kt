package com.example.movies_compose.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.movies_compose.ui.components.LoadingComponent
import com.example.movies_compose.ui.components.MovieCard
import com.example.movies_compose.ui.viewModels.MovieViewModel

@Composable
fun PopularScreen(navigateToDetail: (Int) -> Unit, viewModel: MovieViewModel) {

    val popularMovies = viewModel.movies.collectAsLazyPagingItems()
    val favoriteMovieIds by viewModel.favoriteMovieIds.collectAsState(initial = emptySet())

    viewModel.setCurrentScreenTitle("Popular Movies")

    if (popularMovies.loadState.refresh is LoadState.Loading) {
        LoadingComponent()
    }

    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxSize()
    ) {
        items(
            count = popularMovies.itemCount,
            key = { index -> popularMovies[index]?.id ?: index },
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