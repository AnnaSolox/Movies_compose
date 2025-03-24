package com.example.movies_compose.data.bbdd.models

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.movies_compose.data.api.models.MovieDetail
import com.example.movies_compose.ui.models.MovieRV
/**
 * Modelo que representa una película con sus géneros asociados.
 *
 * Esta clase es usada para obtener una película junto con su lista de géneros desde la base de datos.
 * La clase utiliza Room para gestionar la relación entre las tablas "movies" y "genres" a través de la tabla intermedia "genres_movie".
 *
 * @property movie Objeto que representa la película. Está embebido y mapea la tabla "movies".
 * @property genres Lista de géneros asociados a la película, obtenidos a través de una relación con la tabla "genres".
 */
data class MovieWithGenres (
    @Embedded
    val movie: MovieDB,

    @Relation(
        parentColumn = "movieId",
        entityColumn = "genreId",
        associateBy = Junction(value = GenresMovieCrossRef::class)
    )
    val genres: List<GenreDB>
) {
    /**
     * Convierte la película con sus géneros a un modelo de visualización para la lista de películas.
     *
     * @return Una instancia de [MovieRV] que representa la película con los datos necesarios para su visualización en la UI.
     */
    fun toRvMovie(): MovieRV {
        return MovieRV(
            id = this.movie.movieId,
            title = this.movie.title,
            posterPath = this.movie.posterPath,
            releaseDate = this.movie.releaseDate,
            isFavorite = this.movie.isFavourite
        )
    }

    /**
     * Convierte el modelo de la película con géneros a un modelo de detalle de película.
     *
     * @return Una instancia de [MovieDetail] que contiene información detallada de la película,
     *         incluidos los géneros mapeados al modelo de dominio.
     */
    fun toMovieDetail(): MovieDetail {
        return MovieDetail (
            id = this.movie.movieId,
            title = this.movie.title,
            tagline = this.movie.tagline,
            voteAverage = this.movie.voteAverage,
            genres = this.genres.map { it.toGenre() },
            overview = this.movie.overview,
            backdropPath = this.movie.backdropPath,
            posterPath = this.movie.posterPath,
            releaseDate = this.movie.releaseDate,
            runtime = this.movie.runtime
        )
    }
}