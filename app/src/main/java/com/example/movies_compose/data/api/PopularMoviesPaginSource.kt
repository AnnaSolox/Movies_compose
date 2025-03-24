package com.example.movies_compose.data.api

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movies_compose.data.api.models.MovieListResponse
import com.example.movies_compose.data.repositories.MoviesRepository
import com.example.movies_compose.ui.models.MovieRV
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

/**
 * Fuente para cargar una lista paginada de películas populares desde la API o la base de datos local.
 *
 * Esta clase es utilizada por la librería Paging para cargar datos en páginas desde la red o la base de datos.
 * Obtiene las películas populares desde la API y las guarda en la base de datos si no están presentes.
 *
 * @property moviesRepository Repositorio para interactuar con la API y la base de datos local.
 */
class PopularMoviesPaginSource(
    private val moviesRepository: MoviesRepository
) : PagingSource<Int, MovieRV>() {

    /**
     * Carga una página de películas populares desde la API o la base de datos local.
     * Si la API falla, intenta cargar los datos desde la base de datos local.
     *
     * @param params Parámetros de carga que incluyen la clave de la página.
     * @return Un objeto `LoadResult` que contiene la lista de películas y las claves de la página anterior y siguiente.
     */
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieRV> {
        val page = params.key ?: 1
        var updatedMovieList: List<MovieRV> = emptyList()
        var totalPages = 1
        val language = "${Locale.getDefault().language}-${Locale.getDefault().country}"

        try {
            // Obtener películas de la API
            val response = moviesRepository.getPopularMoviesFromAPI(language, page)
            val favoriteMovieIds = moviesRepository.getFavoriteMovies().map { it.movieId }.toSet()

            updatedMovieList = response.movies.map { movie ->
                movie.toRvMovie().apply {
                    isFavorite = favoriteMovieIds.contains(id)
                }
            }

            // Guardar películas en BBDD en segundo plano
            getMovieDetailsAndSave(language, response, favoriteMovieIds)
            totalPages = response.totalPages

        } catch (e: Exception) {
            Log.e("PAGIN SOURCE", "API_ERROR: Error obteniendo películas desde la API, intentando usar la base de datos local", e)
        }

        // Si la lista está vacía después del intento con la API, buscar en la BBDD
        if (updatedMovieList.isEmpty()) {
            try {
                val movieListFromDB = moviesRepository.getPopularMoviesFromDB()
                if (movieListFromDB.isNotEmpty()) {
                    updatedMovieList = movieListFromDB.map { it.toRvMovie() }
                } else {
                    throw Exception("No hay datos disponibles en la base de datos.")
                }
            } catch (dbException: Exception) {
                Log.e("PAGIN SOURCE", "DB_ERROR: Error al obtener películas desde la base de datos", dbException)
                return LoadResult.Error(dbException)
            }
        }

        return LoadResult.Page(
            data = updatedMovieList,
            prevKey = if (page == 1) null else page - 1,
            nextKey = if (page < totalPages) page + 1 else null
        )
    }

    /**
     * Función para guardar los detalles de las películas en la base de datos de forma asíncrona.
     *
     * @param language El idioma en el que se deben obtener los detalles de las películas.
     * @param response Respuesta de la API con las películas que se deben guardar.
     * @param favoriteMovieIds Conjunto de IDs de películas favoritas.
     */
    private fun getMovieDetailsAndSave(language: String, response: MovieListResponse, favoriteMovieIds: Set<Int>) {
        CoroutineScope(Dispatchers.IO).launch {
            val detailMovieList = response.movies.map{
                    movie ->
                moviesRepository.getMovieByIdFromAPI(language, movie.id)
            }

            Log.d("PAGIN SOURCE", "Películas a guardar en BD: $detailMovieList")

            moviesRepository.saveMoviesToDB(detailMovieList, favoriteMovieIds)
        }
    }

    /**
     * Devuelve la clave de la página de refresco para la paginación.
     *
     * @param state El estado actual de la paginación.
     * @return La clave de la página de refresco.
     */
    override fun getRefreshKey(state: PagingState<Int, MovieRV>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}