package com.example.movies_compose.data.bbdd

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.movies_compose.data.bbdd.models.GenreDB
import com.example.movies_compose.data.bbdd.models.GenresMovieCrossRef
import com.example.movies_compose.data.bbdd.models.MovieDB
import com.example.movies_compose.data.bbdd.models.MovieWithGenres
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz DAO (Data Access Object) que proporciona los métodos necesarios para interactuar con la base de datos Room.
 *
 * Esta interfaz contiene métodos para consultas, inserciones, actualizaciones y obtención de datos relacionados con películas, géneros y las relaciones entre ellas.
 */
@Dao
interface MovieDAO {
    /**
     * Inserta una lista de películas en la base de datos. Si ya existen películas con el mismo ID, se reemplazan.
     *
     * @param movies Lista de objetos `MovieDB` a insertar o reemplazar en la base de datos.
     */
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMovies(movies: List<MovieDB>)

    /**
     * Inserta una lista de géneros en la base de datos. Si ya existen géneros con el mismo ID, se reemplazan.
     *
     * @param genres Lista de objetos `GenreDB` a insertar o reemplazar en la base de datos.
     */
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveGenres(genres: List<GenreDB>)

    /**
     * Inserta una lista de referencias cruzadas entre géneros y películas en la base de datos.
     * Si ya existen registros con las mismas claves, se reemplazan.
     *
     * @param genresMovieCrossRefs Lista de objetos `GenresMovieCrossRef` a insertar o reemplazar.
     */
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveGenresMovieCrossRefs(genresMovieCrossRefs: List<GenresMovieCrossRef>)

    /**
     * Actualiza el campo `isFavourite` de una película en la base de datos.
     *
     * @param movie Objeto `MovieDB` con la película a actualizar.
     */
    @Update
    suspend fun updateFavorite(movie: MovieDB)

    /**
     * Obtiene una película con sus géneros asociados a partir de su ID de película.
     *
     * @param movieId ID de la película a consultar.
     * @return Objeto `MovieWithGenres` que contiene la película y sus géneros asociados.
     */
    @Transaction
    @Query("SELECT * FROM movies WHERE movieId =:movieId")
    suspend fun getMovieWithGenreById(movieId: Int): MovieWithGenres

    /**
     * Obtiene una película por su ID.
     *
     * @param movieId ID de la película a consultar.
     * @return Objeto `MovieDB` que contiene los datos de la película solicitada.
     */
    @Query("SELECT * FROM movies WHERE movieId = :movieId")
    suspend fun getMovieById(movieId: Int): MovieDB

    /**
     * Obtiene una lista paginada de las películas favoritas ordenadas por `movieId`.
     *
     * @return Un objeto `PagingSource` que proporciona acceso paginado a las películas favoritas.
     */
    @Transaction
    @Query("SELECT * FROM movies WHERE isFavourite = 1 ORDER BY movieId ASC")
    fun getFavoritesPagedList(): PagingSource<Int, MovieWithGenres>

    /**
     * Obtiene los IDs de las películas favoritas.
     *
     * @return Un `Flow` que emite una lista de los IDs de las películas favoritas.
     */
    @Query("SELECT movieId FROM movies WHERE isFavourite = 1")
    fun getFavoriteMovieIds(): Flow<List<Int>>

    /**
     * Obtiene una lista de todas las películas favoritas.
     *
     * @return Lista de objetos `MovieDB` que representan las películas favoritas.
     */
    @Query("SELECT * FROM movies WHERE isFavourite = 1 ORDER BY movieId ASC")
    suspend fun getFavoriteMovieById(): List<MovieDB>

    /**
     * Obtiene todas las películas junto con sus géneros asociados, ordenadas por `movieId`.
     *
     * @return Lista de objetos `MovieWithGenres`, cada uno contiene una película y sus géneros asociados.
     */
    @Transaction
    @Query("SELECT * FROM movies ORDER BY movieId ASC")
    suspend fun getMoviesWithGenres(): List<MovieWithGenres>
}