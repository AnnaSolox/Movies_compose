package com.example.movies_compose.core.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.movies_compose.data.api.RetrofitInstance
import com.example.movies_compose.data.bbdd.MovieDatabase
import com.example.movies_compose.data.repositories.MoviesRepository
import com.example.movies_compose.ui.components.BottomNavigationBar
import com.example.movies_compose.ui.components.TopBar
import com.example.movies_compose.ui.screens.FavoriteScreen
import com.example.movies_compose.ui.screens.MovieDetailScreen
import com.example.movies_compose.ui.screens.PopularScreen
import com.example.movies_compose.ui.viewModels.MovieViewModel
import com.example.movies_compose.ui.viewModels.MovieViewModelFactory

@Composable
fun NavigationWrapper() {
    val navController = rememberNavController()

    val moviesRepository = MoviesRepository(
        movieDAO = MovieDatabase.getDatabase(LocalContext.current).movieDao(),
        movieApiService = RetrofitInstance.movieApiService
    )

    val viewModel: MovieViewModel = viewModel(factory = MovieViewModelFactory(moviesRepository))

    val currentTitle = viewModel.currentTitle.observeAsState()

    Scaffold(
        topBar = { currentTitle.value?.let { TopBar(navController = navController, currentTitle = it) } },
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = PopularMovies,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<PopularMovies> {
                viewModel.setCurrentScreenTitle("Popular Movies")
                PopularScreen ({ movieId ->
                    navController.navigate(MovieDetail(movieId = movieId))
                }, viewModel)
            }
            composable<FavoriteMovies> {
                viewModel.setCurrentScreenTitle("Favorite Movies")
                FavoriteScreen ({ movieId ->
                    navController.navigate(MovieDetail(movieId = movieId))
                }, viewModel)
            }
            composable<MovieDetail> { backStackEntry ->
                val movieDetail: MovieDetail = backStackEntry.toRoute()
                MovieDetailScreen(movieId = movieDetail.movieId, viewModel = viewModel)
            }
        }
    }

}