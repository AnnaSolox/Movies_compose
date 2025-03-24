package com.example.movies_compose.ui.models

/**
 * Modelo que representa una película a mostrar en la UI del recycler View.
 *
 */
data class MovieRV (
  val id: Int,
  val title: String?,
  val posterPath: String?,
  val releaseDate: String?,
  var isFavorite: Boolean
)
