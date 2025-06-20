package com.example.movies_compose.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Componente utilizado cuando la pantalla está en estado de carga.
 * @see CircularProgressIndicator Indicador de estado de carga personalizado
 *
 * @Composable
 */
@Preview(showBackground = true)
@Composable
fun LoadingComponent() {
    Box (Modifier.fillMaxSize()){
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
                .size(80.dp),
            strokeWidth = 7.dp
        )
    }
}