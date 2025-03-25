package com.example.movies_compose.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil3.compose.AsyncImage
import com.example.movies_compose.R
import com.example.movies_compose.core.Constants
import com.example.movies_compose.data.api.models.MovieDetail

@Composable
fun MovieMainInformation(
    movie: MovieDetail,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit
) {

    val context = LocalContext.current
    val displayMetrics = context.resources.displayMetrics

    val screenHeight = displayMetrics.heightPixels // en píxeles
    val screenHeightDp = with(LocalDensity.current) { screenHeight.toDp() }

    val vote: Int = (movie.voteAverage?.times(10))?.toInt() ?: 0
    val circularProgressColor = when (vote) {
        0 -> Color.Gray
        in 1..49 -> MaterialTheme.colorScheme.error
        in 50..75 -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.primary
    }

    val favoriteIcon = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder

    val genres = movie.genres?.map { it.name }

    Column(
        Modifier
            .fillMaxWidth()
            .height(screenHeightDp)
    ) {
        ConstraintLayout {
            val information = createRef()

            Box {
                    AsyncImage(
                        model = Constants.BACKDROP_BASE_URL + movie.backdropPath,
                        contentDescription = "Movie backdrop image",
                        error = painterResource(R.drawable.image_not_found),
                        fallback = ColorPainter(Color.DarkGray),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .height(screenHeightDp)
                            .fillMaxWidth()
                            .background(Color.LightGray)
                            .drawWithContent {
                                drawContent()
                                drawRect(
                                    brush = Brush.linearGradient(
                                        0.0f to Color.White.copy(alpha = 1f),
                                        0.4f to Color.White.copy(alpha = 0.9f),
                                        0.8f to Color.Transparent,
                                        start = Offset(0.0f, size.height),
                                        end = Offset(0.0f, size.height * 0.2f)
                                    )
                                )
                            }
                    )
            }

            Column(
                Modifier
                    .padding(25.dp)
                    .constrainAs(information) {
                        bottom.linkTo(parent.bottom)
                    }) {
                Row {
                    movie.releaseDate?.substring(0, 4)?.let {
                        Text(
                            modifier = Modifier
                                .padding(bottom = 16.dp)
                                .weight(1f),
                            text = "($it)",
                            style = MaterialTheme.typography.displaySmall
                        )
                    }
                    Box(
                        Modifier
                            .align(Alignment.Top)
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                            .clickable {
                                onFavoriteClick()
                            }
                    ) {
                        Icon(
                            imageVector = favoriteIcon,
                            tint = Color.White,
                            contentDescription = "FavoriteIcon",
                            modifier = Modifier
                                .size(38.dp)
                                .align(Alignment.Center)
                        )
                    }
                }
                movie.title?.let {
                    Text(
                        text = it.uppercase(),
                        style = MaterialTheme.typography.displayMedium,
                    )
                }

                movie.tagline?.let {
                    Text(
                        modifier = Modifier.padding(bottom = 25.dp),
                        text = it,
                        style = MaterialTheme.typography.displaySmall,
                        fontSize = 18.sp
                    )
                }

                Row(Modifier.fillMaxWidth()) {
                    Column(
                        Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        verticalArrangement = Arrangement.Top,
                    ) {
                        genres?.let {
                            Text(
                                text = it.toString().substringAfter("[").substringBefore("]"),
                                style = MaterialTheme.typography.bodyLarge,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        movie.releaseDate?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        movie.runtime?.let {
                            if (movie.runtime != 0) {
                                Text(
                                    text = "$it min",
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            }
                        }
                    }
                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        Box(
                            contentAlignment = Alignment.CenterEnd,
                            modifier = Modifier.size(55.dp)
                        ) {
                            vote.let {
                                CircularProgressIndicator(
                                    progress = { if (vote != 0) vote.toFloat() / 100 else 100f },
                                    color = circularProgressColor,
                                    trackColor = Color.LightGray,
                                    modifier = Modifier.fillMaxSize(),
                                )
                                Text(
                                    text = if (vote != 0) "${vote}%" else "NR",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MovieOverview(movie: MovieDetail) {
    movie.overview?.let {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 25.dp)
                .padding(bottom = 50.dp)
        ) {
            Column {
                Text(
                    text = "Overview",
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = movie.overview.toString(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
