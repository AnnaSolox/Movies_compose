package com.example.movies_compose.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.movies_compose.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavController, currentTitle: String) {
    val context = LocalContext.current
    val excludedTitlesIcon =
        listOf(
            context.resources.getString(R.string.popular_topbar_title),
            context.resources.getString(R.string.favorites_topbar_title)
        )
    TopAppBar(
        title = { Text(text = currentTitle) },
        navigationIcon = {
            if (currentTitle !in excludedTitlesIcon) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = context.resources.getString(R.string.icon_arrowback_topbar)
                    )
                }
            }
        }
    )
}