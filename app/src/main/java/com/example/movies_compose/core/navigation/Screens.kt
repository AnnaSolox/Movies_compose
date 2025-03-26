package com.example.movies_compose.core.navigation

import kotlinx.serialization.Serializable

/**
 * Objeto que representa la pantalla de "Películas Populares" en la navegación.
 *
 * @Serializable Marca el objeto para que pueda ser serializado.
 */
@Serializable
object PopularMovies

/**
 * Objeto que representa la pantalla de "Películas Favoritas" en la navegación.
 *
 * @Serializable Marca el objeto para que pueda ser serializado.
 */
@Serializable
object FavoriteMovies

/**
 * Data class de la navegación a la pantalla de Detalle de la película.
 * Contiene el identificador único de la película que se pasará como parámetro a la pantalla de detalle.
 *
 * @property movieId El ID de la película que se usará para cargar sus detalles.
 * @Serializable Marca la clase para que pueda ser serializada.
 */
@Serializable
data class MovieDetail(
    val movieId: Int
)