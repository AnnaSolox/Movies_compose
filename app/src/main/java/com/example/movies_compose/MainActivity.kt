package com.example.movies_compose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.movies_compose.ui.models.Routes
import com.example.movies_compose.ui.models.navigationItems
import com.example.movies_compose.ui.screens.FavoriteScreen
import com.example.movies_compose.ui.screens.MovieDetailScreen
import com.example.movies_compose.ui.screens.PopularScreen
import com.example.movies_compose.ui.theme.Movies_composeTheme

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Movies_composeTheme {
                val navigationController = rememberNavController()
                var showBottomBar by remember { mutableStateOf(true) }

                LaunchedEffect(navigationController) {
                    navigationController.currentBackStackEntryFlow.collect { backStackEntry ->
                        showBottomBar = backStackEntry.destination.route != Routes.DetailScreen.route
                    }
                }

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
                        if(showBottomBar){
                            BottomNavigationBar(navigationController)
                        }
                    })
                { innerPadding ->

                    NavHost(
                        navController = navigationController,
                        startDestination = Routes.PopularScreen.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Routes.PopularScreen.route) { PopularScreen(navigationController) }
                        composable(Routes.FavoriteScreen.route) {
                            FavoriteScreen(
                                navigationController
                            )
                        }
                        composable(
                            Routes.DetailScreen.route,
                            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            MovieDetailScreen(
                                navigationController,
                                backStackEntry.arguments?.getInt("movieId") ?: 0
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val selectedNavigationIndex = rememberSaveable { mutableIntStateOf(0) }

    NavigationBar(
        containerColor = Color.White
    ) {
        navigationItems.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedNavigationIndex.intValue == index,
                onClick = {
                    selectedNavigationIndex.intValue = index
                    navController.navigate(item.route)
                },
                icon = {
                    Icon(imageVector = item.icon, contentDescription = item.title)
                },
                label = {
                    Text(
                        item.title,
                        color = if (index == selectedNavigationIndex.intValue)
                            Color.Black
                        else Color.Gray
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.surface,
                    indicatorColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}