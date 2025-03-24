package com.example.movies_compose.data.api.models

import com.google.gson.annotations.SerializedName

/**
 * Modelo que representa la respuesta de la API para el listado de películas populares.
 *
 * Contiene información básica sobre el número de pagina, la lista de películas [MovieResponse], el total de páginas y el total de resultados.
 *
 */
data class MovieListResponse(
    val page: Int,
    @SerializedName("results")
    val movies: List<MovieResponse>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)