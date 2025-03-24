package com.example.movies_compose.data.api.models

import com.example.movies_compose.ui.models.MovieRV
import com.google.gson.annotations.SerializedName

/**
 * Modelo que representa la respuesta de la API para una película popular.
 *
 * Contiene información básica sobre la película, como su título, voto promedio, géneros, etc.
 *
 */
data class MovieResponse (
    val id: Int,
    val title: String?,
    @SerializedName("vote_average")
    val voteAverage: Double?,
    @SerializedName("genre_ids")
    val genres: List<Int>?,
    val overview: String?,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("release_date")
    val releaseDate: String?,
) {
    /**
     * Devuelve una representación en forma de cadena de texto de este objeto.
     *
     * @return Una cadena que describe los atributos de [MovieResponse].
     */
    override fun toString(): String {
        return """
            MovieResponse:
            ID: $id
            Title: $title
            Vote Average: $voteAverage
            Genres: $genres
            Overview: $overview
            Backdrop path: $backdropPath
            Poster path: $posterPath
            Release Date: $releaseDate
        """.trimIndent()
    }

    /**
     * Convierte un modelo MovieResponse en un modelo MovieRV para su uso en la interfaz de usuario.
     *
     * @return Una instancia de [MovieRV] para su uso en la UI.
     */
    fun toRvMovie(): MovieRV {
        return MovieRV(
            id = this.id,
            title = this.title,
            posterPath = this.posterPath,
            releaseDate = this.releaseDate,
            isFavorite = false
        )
    }
}