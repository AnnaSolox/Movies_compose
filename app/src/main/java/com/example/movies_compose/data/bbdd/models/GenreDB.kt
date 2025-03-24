package com.example.movies_compose.data.bbdd.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.movies_compose.data.api.models.Genre

/**
 * Modelo que representa la respuesta de la BBDD para un género.
 *
 * Esta clase es una entidad de Room que representa la tabla "genres" en la base de datos.
 * Cada instancia de esta clase contiene un identificador único para el género y su nombre.
 *
 */
@Entity(tableName = "genres")
data class GenreDB(
    @PrimaryKey
    val genreId: Int,
    val name: String
) {
    /**
     * Convierte el modelo del género obtenido desde base de datos al modelo a utilizar por la API
     *
     * @return Una instancia de [Genre]
     */
    fun toGenre(): Genre {
        return Genre(
            id = this.genreId,
            name = this.name
        )
    }
}