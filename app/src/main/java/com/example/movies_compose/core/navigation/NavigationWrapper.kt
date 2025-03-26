package com.example.movies_compose.core.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.movies_compose.R
import com.example.movies_compose.data.api.RetrofitInstance
import com.example.movies_compose.data.bbdd.MovieDatabase
import com.example.movies_compose.data.repositories.MoviesRepository
import com.example.movies_compose.ui.components.TabsComponent
import com.example.movies_compose.ui.components.TopBar
import com.example.movies_compose.ui.screens.FavoriteScreen
import com.example.movies_compose.ui.screens.MovieDetailScreen
import com.example.movies_compose.ui.screens.PopularScreen
import com.example.movies_compose.ui.viewModels.MovieViewModel
import com.example.movies_compose.ui.viewModels.MovieViewModelFactory

/**
 * Función Composable que gestiona la navegación entre pantallas de la aplicación.
 * Incluye una barra superior que muestra el título de la pantalla actual y una barra inferior con pestañas
 * para navegar entre películas populares y favoritas.
 *
 * Si la pantalla no corresponde con favoritos ni con populares, la barra inferior desaparece.
 *
 * @Composable
 */
@Composable
fun NavigationWrapper() {
    val navController = rememberNavController()

    val context = LocalContext.current

    val moviesRepository = MoviesRepository(
        movieDAO = MovieDatabase.getDatabase(LocalContext.current).movieDao(),
        movieApiService = RetrofitInstance.movieApiService
    )

    val viewModel: MovieViewModel = viewModel(factory = MovieViewModelFactory(moviesRepository))

    val currentTitle = viewModel.currentTitle.observeAsState()
    var tabVisibilityState by rememberSaveable {( mutableStateOf( true ))}

    Scaffold(
        topBar = {
            currentTitle.value?.let {
                TopBar(
                    navController = navController,
                    currentTitle = it
                )
            }
        },
        bottomBar = {
            if (tabVisibilityState) {
                BottomAppBar(
                    containerColor = Color.White
                ) {
                    TabsComponent(navController = navController, viewModel = viewModel)
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = PopularMovies,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<PopularMovies> {
                viewModel.setCurrentScreenTitle(context.resources.getString(R.string.popular_topbar_title))
                viewModel.selectIndex(0)
                tabVisibilityState = true
                PopularScreen({ movieId ->
                    navController.navigate(MovieDetail(movieId = movieId))
                }, viewModel)
            }
            composable<FavoriteMovies> {
                viewModel.setCurrentScreenTitle(context.resources.getString(R.string.favorites_topbar_title))
                viewModel.selectIndex(1)
                tabVisibilityState = true
                FavoriteScreen({ movieId ->
                    navController.navigate(MovieDetail(movieId = movieId))
                }, viewModel)
            }
            composable<MovieDetail> { backStackEntry ->
                tabVisibilityState = false
                val movieDetail: MovieDetail = backStackEntry.toRoute()
                MovieDetailScreen(movieId = movieDetail.movieId, viewModel = viewModel)
            }
        }
    }
}