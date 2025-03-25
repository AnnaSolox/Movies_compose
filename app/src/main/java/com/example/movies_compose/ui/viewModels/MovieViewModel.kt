package com.example.movies_compose.ui.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.movies_compose.data.api.PopularMoviesPaginSource
import com.example.movies_compose.data.api.models.MovieDetail
import com.example.movies_compose.data.bbdd.models.MovieDB
import com.example.movies_compose.data.repositories.MoviesRepository
import com.example.movies_compose.ui.models.MovieRV
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

/**
 * ViewModel para manejar la lógica relacionada con las películas y sus detalles.
 * Proporciona acceso a los datos de las películas, incluyendo películas populares, favoritas y detalles de una película específica.
 *
 * @param movieRepository Repositorio de películas que interactúa con la base de datos y las API externas.
 */
class MovieViewModel(private val movieRepository: MoviesRepository) : ViewModel() {

    //Detectar idioma del teléfono
    private val language = "${Locale.getDefault().language}-${Locale.getDefault().country}"

    // Estado de carga
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    /**
     * Flujo que emite un conjunto de IDs de películas favoritas.
     */
    val favoriteMovieIds: Flow<Set<Int>> = movieRepository.getFavoriteMovieIds()

    /**
     * Flujo que emite las películas populares para mostrar en el RecyclerView.
     * Utiliza paginación para cargar las películas en bloques.
     */
    val movies: Flow<PagingData<MovieRV>> = Pager(
        config = PagingConfig(
            pageSize = 20,  // Se actualizará automáticamente con el tamaño de `movies`
            prefetchDistance = 5, // Cargar la siguiente página cuando queden 5 elementos visibles
            enablePlaceholders = false
        ),
        pagingSourceFactory = { PopularMoviesPaginSource(movieRepository) }
    ).flow.cachedIn(viewModelScope)

    /**
     * Flujo que emite las películas populares para mostrar en el RecyclerView.
     * Utiliza paginación para cargar las películas en bloques.
     */
    val favorites: Flow<PagingData<MovieRV>> =
        movieRepository.getFavoritePagedMovies().map { it -> it.map { it.toRvMovie() } }
            .cachedIn(viewModelScope)

    //Info del detalle de la película
    private val _movie = MutableLiveData<MovieDetail?>()
    val movie: LiveData<MovieDetail?> get() = _movie

    //Gestión de errores
    private val _errorMessage = MutableLiveData<String>()
    val error: LiveData<String> get() = _errorMessage

    /**
     * Función para limpiar la información de la película almacenada en el ViewModel.
     */
    fun clearMovie(){
        _movie.value = null
    }

    /**
     * Función para obtener los detalles de una película específica por su ID.
     * Intenta obtener los datos desde la API, y si falla, intenta obtenerlos desde la base de datos.
     *
     * @param movieId ID de la película cuya información se desea obtener.
     */
    fun fetchMovieById(movieId: Int) {
        _isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            var movieDetails: MovieDetail? = null

            try {
                // Intentar obtener la película de la API
                movieDetails = movieRepository.getMovieByIdFromAPI(language, movieId)
                Log.d("VIEWMODEL", "Película con id $movieId cargada desde API: \nPelícula: $movieDetails")
            } catch (e: Exception) {
                Log.e("VIEWMODEL", "Error al obtener película desde la API: ", e)
            }

            // Si no obtenemos la película desde la API, intentamos buscarla en la base de datos
            if (movieDetails == null) {
                try {
                    val movieFromDb = movieRepository.getMovieWithGenresByIdFromDB(movieId)

                    // Actualizamos el UI en el hilo principal con los datos de la base de datos
                    withContext(Dispatchers.Main) {
                        _isLoading.value = false
                        _movie.value = movieFromDb.toMovieDetail()
                        Log.d("VIEWMODEL", "Película obtenida de la base de datos: $movieFromDb")
                    }
                } catch (dbException: Exception) {
                    // Si la consulta a la base de datos también falla
                    withContext(Dispatchers.Main) {
                        _isLoading.value = false
                        _errorMessage.value = "Error al acceder a la base de datos."
                    }
                    Log.e("VIEWMODEL", "Error al obtener película desde la base de datos: ", dbException)
                }
            } else {
                // Si se obtuvo la película desde la API, la mostramos
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                    _movie.value = movieDetails
                    Log.d("VIEWMODEL", "Película obtenida desde la API: $movieDetails")
                }
            }
        }
    }

    /**
     * Función para actualizar el estado de favoritos de una película en la base de datos.
     *
     * @param movie Objeto `MovieDB` que representa la película a actualizar en la base de datos.
     */
    fun updateFavorites(movie: MovieDB) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                movieRepository.updateFavorites(movie)
            } catch (e: Exception) {
                Log.e("VIEWMODEL", "Error al actualizar el favorito: ${e.message}")
            }
        }
    }

    /**
     * Función suspendida para obtener una película desde la base de datos por su ID.
     *
     * @param movieId ID de la película que se desea obtener.
     * @return Un objeto `MovieDB` con los detalles de la película, o `null` si no se encuentra.
     */
    fun getMovieByIdFromDb(movieId: Int, onResult: (MovieDB?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val movieDB = movieRepository.getMovieByIdFromDB(movieId)
                withContext(Dispatchers.Main) {
                    onResult(movieDB)
                }
            } catch (e: Exception) {
                Log.e("VIEWMODEL", "Error al obtener la película con id $movieId desde BBDD: ${e.message}")
                withContext(Dispatchers.Main) {
                    onResult(null)
                }
            }
        }
    }
}
