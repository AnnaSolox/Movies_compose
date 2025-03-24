package com.example.movies_compose.ui.models

sealed class Routes (val route: String) {
    object PopularScreen: Routes ("popularScreen")
    object FavoriteScreen: Routes ("favoriteScreen")
    object DetailScreen: Routes ("detailScreen/{movieId}"){
        fun createRoute(movieId: Int) = "detaukScreen/$movieId"
    }
}