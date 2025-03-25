package com.example.movies_compose.data.api

import com.example.movies_compose.data.api.models.MovieDetail
import com.example.movies_compose.data.api.models.MovieListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interfaz con los métodos de consulta a la API TheMovieDB
 */
interface MovieApiService {

    /**
     * Consulta para obtener la lista de películas populares
     *
     * @param language Idioma en el que se obtendrá la consulta
     * @param page número de la página a consultar
     *
     * @return Retrofit Response que contiene un objeto `MovieListResponse` con la lista de películas.
     */
    @GET("movie/popular?language=")
    suspend fun getPopularMovies(
        @Query("language") language: String,
        @Query("page") page: Int
    ): Response<MovieListResponse>

    /**
     * Consulta para obtener una película según su ID
     *
     * @param id Id de la película a obtener
     * @param language Idioma en el que se obtendrá la consulta
     *
     * @return Retrofit Response que contiene un objeto `MovieDetail` con los detalles de la película
     */
    @GET("movie/{movie_id}")
    suspend fun getMovieById(
        @Path("movie_id") id: Int,
        @Query("language") language: String
    ): Response<MovieDetail>
}