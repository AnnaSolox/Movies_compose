package com.example.movies_compose.data.bbdd.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Modelo que representa la respuesta de la BBDD para una película.
 *
 * Esta clase es una entidad de Room que representa la tabla "movies" en la base de datos.
 *
 * La clase contiene todos los detalles de la película, incluyendo si es `favorita` o no.
 *
 */
@Entity(tableName = "movies")
data class MovieDB (
    @PrimaryKey
    val movieId: Int,
    val title: String?,
    val tagline: String?,
    @ColumnInfo("vote_average")
    val voteAverage: Double?,
    val overview: String?,
    @ColumnInfo("backdrop_path")
    val backdropPath: String?,
    @ColumnInfo("poster_path")
    val posterPath: String?,
    @ColumnInfo("release_date")
    val releaseDate: String?,
    val runtime: Int?,
    var isFavourite: Boolean
) {
    /**
     * Devuelve una representación en forma de cadena de texto de este objeto.
     *
     * @return Una cadena que describe los atributos de la película de la base de datos.
     */
    override fun toString(): String {
        return """
            MovieDB:
            ID: $movieId
            Title: $title
            Tagline: $tagline
            Vote Average: $voteAverage
            Overview: $overview
            Backdrop path: $backdropPath
            Poster path: $posterPath
            Release Date: $releaseDate
            Runtime: $runtime min
            Favourite: $isFavourite
        """.trimIndent()
    }
}

