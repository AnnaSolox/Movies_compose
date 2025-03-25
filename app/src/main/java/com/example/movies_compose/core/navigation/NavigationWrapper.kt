package com.example.movies_compose.core.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.movies_compose.ui.components.BottomNavigationBar
import com.example.movies_compose.ui.screens.FavoriteScreen
import com.example.movies_compose.ui.screens.MovieDetailScreen
import com.example.movies_compose.ui.screens.PopularScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationWrapper() {
    val navController = rememberNavController()

    Scaffold(
        topBar = {TopAppBar(title = { Text("Popular Movies") })},
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = PopularMovies,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<PopularMovies> {
                PopularScreen { movieId ->
                    navController.navigate(MovieDetail(movieId = movieId))
                }
            }
            composable<FavoriteMovies> {
                FavoriteScreen { movieId ->
                    navController.navigate(MovieDetail(movieId = movieId))
                }
            }
            composable<MovieDetail> { backStackEntry ->
                val movieDetail: MovieDetail = backStackEntry.toRoute()
                MovieDetailScreen(movieId = movieDetail.movieId)
            }
        }
    }

}