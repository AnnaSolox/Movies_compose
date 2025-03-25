package com.example.movies_compose.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.movies_compose.ui.components.LoadingComponent
import com.example.movies_compose.ui.components.MovieCard
import com.example.movies_compose.ui.components.NoFavorites
import com.example.movies_compose.ui.viewModels.MovieViewModel

@Composable
fun FavoriteScreen(navigateToDetail: (Int) -> Unit, viewModel: MovieViewModel) {

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