package com.example.movies_compose.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movies_compose.data.repositories.MoviesRepository

/**
 * Fábrica para crear instancias de `MovieViewModel`.
 * Se utiliza para proporcionar la instancia de `MoviesRepository` al ViewModel.
 *
 * @param moviesRepository Repositorio de películas que interactúa con la base de datos y las API externas.
 */
class MovieViewModelFactory(
    private val moviesRepository: MoviesRepository
) : ViewModelProvider.Factory {

    /**
     * Crea una nueva instancia de `MovieViewModel` utilizando el repositorio de películas.
     *
     * @param modelClass La clase del ViewModel que se debe crear.
     * @return Una instancia de `MovieViewModel` si la clase es compatible, de lo contrario lanza una excepción.
     *
     * @throws IllegalArgumentException Si el `modelClass` no es una subclase de `MovieViewModel`.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovieViewModel(moviesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
