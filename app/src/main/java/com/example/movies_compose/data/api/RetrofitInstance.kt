package com.example.movies_compose.data.api

import MovieApiService
import com.example.movies_compose.core.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton que gestiona la instancia de [Retrofit] para las solicitudes a la API.
 *
 * Utiliza un [OkHttpClient] con un interceptor de autenticación y un [GsonConverterFactory] para convertir las respuestas JSON.
 *
 * Proporciona una única instancia de `MovieApiService`.
 */
object RetrofitInstance {

    //Crear y mantener una única instancia de RETROFIT

    //Cliente OkHttp con el token de acceso
    private val client = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(Constants.API_KEY))
        .build()

    //Builder Retrofit
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //Proporcionar el servicio de la API
    val movieApiService: MovieApiService by lazy {
        retrofit.create(MovieApiService::class.java)
    }
}