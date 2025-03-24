package com.example.movies_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.movies_compose.ui.components.CardsScreen
import com.example.movies_compose.ui.components.MovieDetails
import com.example.movies_compose.ui.models.Routes
import com.example.movies_compose.ui.theme.Movies_composeTheme

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Movies_composeTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary,
                            ),
                            title = {
                                Text("Popular Movies")
                            }
                        )
                    },
                    bottomBar = {

                    })
                { innerPadding ->
                    val navigationController = rememberNavController()
                    NavHost(
                        navController = navigationController,
                        startDestination = Routes.PopularScreen.route
                    ) {
                        composable(Routes.PopularScreen.route) {
                            CardsScreen(
                                navigationController,
                                Modifier.padding(innerPadding)
                            )
                        }
                        composable(Routes.DetailScreen.route) {
                            MovieDetails(
                                navigationController,
                                Modifier.padding(innerPadding)
                            )
                        }
                    }
                }
            }
        }
    }
}