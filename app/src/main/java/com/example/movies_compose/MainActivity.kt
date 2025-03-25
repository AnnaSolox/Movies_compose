package com.example.movies_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.movies_compose.core.navigation.NavigationWrapper
import com.example.movies_compose.ui.theme.Movies_composeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Movies_composeTheme (dynamicColor = false) {
                NavigationWrapper()
            }
        }
    }
}

