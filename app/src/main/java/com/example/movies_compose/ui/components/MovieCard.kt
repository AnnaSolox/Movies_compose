package com.example.movies_compose.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil3.compose.AsyncImage
import com.example.movies_compose.core.Constants
import com.example.movies_compose.ui.models.MovieRV


@Composable
fun MovieCard(
    movieRV: MovieRV,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onFavoriteClick: (Boolean) -> Unit
) {

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp

    val heightInDp = screenHeight * 0.6

    val brush = Brush.linearGradient(
        0.0f to Color.Black.copy(alpha = 0.8f),
        .6f to Color.Transparent,
        start = Offset(0.0f, 1300.0f),
        end = Offset(0.0f, 000.0f)
    )

    val favoriteIcon = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder

    ElevatedCard(
        Modifier
            .fillMaxWidth()
            .height(heightInDp.dp)
            .padding(horizontal = 5.dp, vertical = 8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.elevatedCardElevation(8.dp),
    ) {
        ConstraintLayout {
            val (gradient, info) = createRefs()
            AsyncImage(
                model = Constants.POSTER_BASE_URL + movieRV.posterPath,
                contentDescription = "Movie poster",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape = MaterialTheme.shapes.medium)
            )
            Box(modifier = Modifier
                .fillMaxSize()
                .background(brush)
                .constrainAs(gradient) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
            )
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .constrainAs(info) {
                        start.linkTo(gradient.start)
                        end.linkTo(gradient.end)
                        bottom.linkTo(gradient.bottom)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(.75f)) {
                    movieRV.title?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White
                        )
                    }

                    movieRV.releaseDate?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White
                        )
                    }
                }

                Column(
                    Modifier.weight(.25f),
                    horizontalAlignment = Alignment.End
                ) {
                    Icon(
                        imageVector = favoriteIcon,
                        contentDescription = "Favorite icon",
                        tint = Color.White,
                        modifier = Modifier
                            .size(40.dp)
                            .clickable {
                                onFavoriteClick(movieRV.isFavorite)
                            }
                    )
                }
            }
        }
    }
}