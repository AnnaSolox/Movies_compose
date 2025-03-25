package com.example.movies_compose.data.repositories

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.movies_compose.data.api.MovieApiService
import com.example.movies_compose.data.api.models.MovieDetail
import com.example.movies_compose.data.api.models.MovieListResponse
import com.example.movies_compose.data.bbdd.MovieDAO
import com.example.movies_compose.data.bbdd.models.GenresMovieCrossRef
import com.example.movies_compose.data.bbdd.models.MovieDB
import com.example.movies_compose.data.bbdd.models.MovieWithGenres
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repositorio que gestiona la obtención, almacenamiento y actualización de películas desde la API y la base de datos.
 *
 * Esta clase interactúa con la API externa para obtener datos sobre películas populares y detalles de películas, y con la base de datos local para almacenar las películas, géneros y relaciones entre películas y géneros. También gestiona las películas favoritas del usuario, tanto a nivel de API como de base de datos.
 *
 * @param movieDAO Instancia de la interfaz DAO para interactuar con la base de datos Room.
 * @param movieApiService Instancia del servicio API para realizar peticiones a la API externa.
 */
class MoviesRepository(
    private val movieDAO: MovieDAO,
    private val movieApiService: MovieApiService,
) {

    /**
     * Obtiene las películas populares desde la API externa.
     *
     * @param language Idioma en el que se deben obtener las películas.
     * @param page Página de resultados que se desea obtener.
     * @return La respuesta de la API que contiene la lista de películas populares.
     * @throws Exception Si ocurre un error al obtener las películas desde la API.
     */
    suspend fun getPopularMoviesFromAPI(language: String, page: Int): MovieListResponse {
        val response = movieApiService.getPopularMovies(language, page)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("La lista de película es nula")
        } else {
            throw Exception("Error: ${response.code()} - ${response.message()}")
        }
    }

    /**
     * Obtiene los detalles de una película desde la API externa.
     *
     * @param language Idioma en el que se deben obtener los detalles de la película.
     * @param id ID de la película cuya información se desea obtener.
     * @return `MovieDetail` con los detalles de la película obtenidos de la API.
     * @throws Exception Si ocurre un error al obtener la película desde la API.
     */
    suspend fun getMovieByIdFromAPI(language: String, id: Int): MovieDetail {
        val response = movieApiService.getMovieById(id, language)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("La película con ID $id es nula")
        } else {
            throw Exception("Error: ${response.code()} - ${response.message()}")
        }
    }

    /**
     * Guarda una lista de películas en la base de datos local, junto con los géneros y relaciones entre géneros y películas.
     *
     * @param movies Lista de objetos `MovieDetail` que representan las películas a almacenar.
     * @param favorites Conjunto de IDs de películas marcadas como favoritas.
     */
    suspend fun saveMoviesToDB(movies: List<MovieDetail>, favorites: Set<Int>) {
        // Mapear MovieResponse a MovieDB, preservando el estado de favorito
        Log.d("REPOSITORY", "Películas recibidas: $movies")
        val movieDBList = movies.map { movie ->
            movie.toEntity().apply {
                // Mantener el estado de favorito si ya existe
                isFavourite = favorites.contains(movieId)
            }
        }

        movieDAO.saveMovies(movieDBList)
        Log.d("REPOSITORY", "Películas guardadas: $movieDBList")

        saveGenres(movies)
        saveGenresMovieCrossRef(movies)
    }

    /**
     * Guarda los géneros de las películas en la base de datos local.
     *
     * @param movies Lista de objetos `MovieDetail` que contienen los géneros a guardar.
     */
    private suspend fun saveGenres(movies: List<MovieDetail>){
        val genresDBList = movies.flatMap { movie ->
            movie.genres?.map { it.toEntity() } ?: emptyList()
        }

        Log.d("REPOSITORY", "Géneros guardados: $genresDBList")

        movieDAO.saveGenres(genresDBList)
    }

    /**
     * Guarda las relaciones entre películas y géneros en la base de datos local.
     *
     * @param movies Lista de objetos `MovieDetail` que contienen las relaciones entre géneros y películas.
     */
    private suspend fun saveGenresMovieCrossRef(movies: List<MovieDetail>){
        val genresMovieCrossRef = movies.flatMap { movie ->
            movie.genres?.map { genre ->
                GenresMovieCrossRef(movie.id, genre.id)
            } ?: emptySet()
        }
        movieDAO.saveGenresMovieCrossRefs(genresMovieCrossRef)
        Log.d("REPOSITORY", "Crossfrefs guardados: $genresMovieCrossRef")
    }

    /**
     * Obtiene todas las películas con sus géneros desde la base de datos local.
     *
     * @return Lista de `MovieWithGenres` de películas con sus géneros almacenadas en la base de datos.
     */
    suspend fun getPopularMoviesFromDB(): List<MovieWithGenres> {
        return movieDAO.getMoviesWithGenres()
    }

    /**
     * Obtiene una película con sus géneros desde la base de datos local por su ID.
     *
     * @param id ID de la película que se desea obtener.
     * @return `MovieWithGenres` de la película con su relación a géneros almacenada en la base de datos.
     */
    suspend fun getMovieWithGenresByIdFromDB(id: Int): MovieWithGenres {
        return movieDAO.getMovieWithGenreById(id)
    }

    /**
     * Obtiene una película desde la base de datos local por su ID.
     *
     * @param id ID de la película que se desea obtener.
     * @return `MovieDB` con la película almacenada en la base de datos.
     */
    suspend fun getMovieByIdFromDB(id: Int): MovieDB {
        return movieDAO.getMovieById(id)
    }

    /**
     * Actualiza el estado de favorito de una película en la base de datos local.
     *
     * @param movie Objeto `MovieDB` con la película y el nuevo estado de favorito.
     */
    suspend fun updateFavorites(movie: MovieDB) {
        movieDAO.updateFavorite(movie)
    }

    /**
     * Obtiene una lista paginada de las películas favoritas desde la base de datos local.
     *
     * @return Un flujo de datos paginados de películas con géneros.
     */
    fun getFavoriteMovieIds(): Flow<Set<Int>> {
        return movieDAO.getFavoriteMovieIds().map { it.toSet() }
    }

    /**
     * Obtiene todas las películas favoritas desde la base de datos local.
     *
     * @return Una lista de películas marcadas como favoritas.
     */
    fun getFavoritePagedMovies(): Flow<PagingData<MovieWithGenres>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { movieDAO.getFavoritesPagedList() }
        ).flow
    }

    /**
     * Obtiene una película favorita desde la base de datos local a través de su ID.
     *
     * @return Una lista favorita de la base de datos.
     */
    suspend fun getFavoriteMovies(): List<MovieDB> {
        return movieDAO.getFavoriteMovieById()
    }

}