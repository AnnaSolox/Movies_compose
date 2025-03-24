package com.example.movies_compose.data.bbdd

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.movies_compose.data.bbdd.models.GenreDB
import com.example.movies_compose.data.bbdd.models.GenresMovieCrossRef
import com.example.movies_compose.data.bbdd.models.MovieDB

/**
 * Base de datos Room para la aplicación de películas.
 *
 * Esta clase define la base de datos que contiene las entidades relacionadas con las películas y géneros.
 * La base de datos incluye las entidades: [MovieDB], [GenreDB] y [GenresMovieCrossRef].
 * La versión actual de la base de datos es la 1.
 *
 * Proporciona un acceso único y global a la base de datos mediante el patrón Singleton, garantizando que solo haya
 * una instancia activa en todo momento.
 */
@Database(entities = [MovieDB::class, GenreDB::class, GenresMovieCrossRef::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {
    /**
     * Método abstracto para obtener el DAO de películas.
     *
     * @return El DAO de películas para realizar operaciones en la base de datos.
     */
    abstract fun movieDao(): MovieDAO

    /**
     * Singleton para obtener la instancia de la base de datos.
     *
     * Si la instancia no ha sido creada, la crea y la guarda.
     *
     * @param El contexto de la aplicación.
     * @return La instancia de la base de datos.
     */
    companion object {
        @Volatile
        private var INSTANCE: MovieDatabase? = null

        // Método para obtener la instancia de la base de datos
        fun getDatabase(context: Context): MovieDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java,
                    "movies_room_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}