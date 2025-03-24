package com.example.movies_compose.data.bbdd.models

import androidx.room.Entity

/**
 * Data class intermedia para gestionar las asociaciones de géneros con películas
 *
 * Esta clase es una entidad de Room que representa la tabla intermedia "genres_movies", que contiene la relación many2many entre géneros y películas en la base de datos.
 *
 */
@Entity(
    tableName = "genres_movie",
    primaryKeys = ["movieId", "genreId"]
)
data class GenresMovieCrossRef(
    val movieId: Int,
    val genreId: Int
)