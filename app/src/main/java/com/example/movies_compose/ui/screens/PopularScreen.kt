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
import com.example.movies_compose.ui.viewModels.MovieViewModel

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
