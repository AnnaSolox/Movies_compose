package com.example.movies_compose.data.api.models

import com.example.movies_compose.data.bbdd.models.GenreDB

/**
 * Modelo que representa la respuesta de la API para una género.
 *
 */
data class Genre(
    val id: Int,
    val name: String
) {

    /**
     * Convierte un modelo Genre en un modelo GenreDB para guardarlo en la base de datos.
     *
     * @return Una instancia de [GenreDB]
     */
    fun toEntity(): GenreDB {
        return GenreDB(
            genreId = this.id,
            name = this.name
        )
    }
}