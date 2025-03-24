package com.example.movies_compose.data.api

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * Interceptor que automatiza la autenticación de las solicitudes API mediante un token Bearer.
 *
 * Este interceptor añade un encabezado de autorización a cada solicitud realizada por Retrofit. El encabezado incluye un token de acceso que es necesario para autenticar las solicitudes.
 *
 * @param token El token de autenticación que se usará en el encabezado "Authorization" de cada solicitud.
 */
class AuthInterceptor(private val token: String) : Interceptor {

    /**
     * Intercepta la solicitud para agregar el encabezado de autenticación.
     *
     * @param chain La cadena de interceptores que permite modificar la solicitud antes de enviarla.
     * @return Respuesta de la solicitud procesada con el encabezado de autenticación agregado.
     */
    override fun intercept (chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()
        val newRequest: Request = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

        // Imprimir encabezados de la respuesta
        newRequest.headers.forEach { header ->
            Log.d("AuthInterceptor", "Header: ${header.first} = ${header.second}")
        }

        return chain.proceed(newRequest)
    }
}