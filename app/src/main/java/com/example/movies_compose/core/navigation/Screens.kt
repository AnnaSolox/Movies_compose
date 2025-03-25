package com.example.movies_compose.core.navigation

import kotlinx.serialization.Serializable

@Serializable
object PopularMovies

@Serializable
object FavoriteMovies

@Serializable
data class MovieDetail(
    val movieId: Int
)