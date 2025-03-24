package com.example.movies_compose.data.api.models

import com.example.movies_compose.data.bbdd.models.MovieDB
import com.google.gson.annotations.SerializedName

/**
 * Modelo que representa la respuesta de la API para una película concreta.
 *
 * Contiene información detallada sobre la película, como su título, subtítulo, voto promedio, géneros, vista general, fecha lanzamiento, etc.
 *
 */
data class MovieDetail (
  val id: Int,
  val title: String?,
  val tagline: String?,
  @SerializedName("vote_average")
  val voteAverage: Double?,
  val genres: List<Genre>?,
  val overview: String?,
  @SerializedName("backdrop_path")
  val backdropPath: String?,
  @SerializedName("poster_path")
  val posterPath: String?,
  @SerializedName("release_date")
  val releaseDate: String?,
  val runtime: Int?
) {
  /**
   * Devuelve una representación en forma de cadena de texto de este objeto.
   *
   * @return Una cadena que describe los atributos de [MovieDetail].
   */
  override fun toString(): String {
    return """
            MovieDetail:
            ID: $id
            Title: $title
            Tagline: $tagline
            Vote Average: $voteAverage
            Genres: $genres
            Overview: $overview
            Backdrop path: $backdropPath
            Poster path: $posterPath
            Release Date: $releaseDate
            Runtime: $runtime min
        """.trimIndent()
  }

  /**
   * Convierte un modelo MovieDetail en un modelo MovieDB para guardarlo en base de datos.
   *
   * @return Una instancia de [MovieDB].
   */
  fun toEntity(): MovieDB {
    return MovieDB(
      movieId = this.id,
      title = this.title,
      tagline = this.tagline,
      voteAverage = this.voteAverage,
      overview = this.overview,
      backdropPath = this.backdropPath,
      posterPath = this.posterPath,
      releaseDate = this.releaseDate,
      runtime = this.runtime,
      isFavourite = false
    )
  }

}


