package com.example.movies_compose.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil3.compose.AsyncImage
import com.example.movies_compose.core.Constants
import com.example.movies_compose.data.api.models.MovieDetail

@Composable
fun MovieMainInformation(movie: MovieDetail) {

    val context = LocalContext.current
    val displayMetrics = context.resources.displayMetrics

    val screenHeight = displayMetrics.heightPixels // en píxeles
    val screenHeightDp = with(LocalDensity.current) { screenHeight.toDp() }

    val whiteGradient = Brush.linearGradient(
        0.0f to Color.White,
        .6f to Color.Transparent,
        start = Offset(0.0f, 1500.0f),
        end = Offset(0.0f, 000.0f)
    )

    val vote: Int = (movie.voteAverage?.times(10))?.toInt() ?: 0
    val circularProgressColor = when (vote){
        0 -> Color.Gray
        in 1..49 -> Color.Red
        in 50..75 -> Color.Yellow
        else -> Color.Green
    }

    val genres = movie.genres?.map { it.name }

    Column(
        Modifier
            .fillMaxWidth()
            .height(screenHeightDp)
    ) {
        ConstraintLayout {
            val information = createRef()

            Box(Modifier.fillMaxSize()) {
                movie.backdropPath?.let {
                    AsyncImage(
                        model = Constants.BACKDROP_BASE_URL + it,
                        contentDescription = "Movie backdrop image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(whiteGradient)
                )
            }

            Column(
                Modifier
                    .padding(25.dp)
                    .constrainAs(information) {
                        bottom.linkTo(parent.bottom)
                    }) {
                Row {
                    movie.releaseDate?.substring(0,4)?.let {
                        Text(
                            modifier = Modifier
                                .padding(bottom = 16.dp)
                                .weight(1f),
                            text = it,
                            style = MaterialTheme.typography.displaySmall
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "FavoriteIcon",
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.Top)
                    )
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
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        genres?.let {
                            Text(
                                text = it.toString().substringAfter("[").substringBefore("]"),
                                style = MaterialTheme.typography.bodyLarge,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        movie.releaseDate?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodyMedium,
                                fontSize = 18.sp
                            )
                        }
                        movie.runtime?.let {
                            Text(
                                text = "$it min",
                                style = MaterialTheme.typography.bodyMedium,
                                fontSize = 18.sp
                            )
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
                                    progress = { if(vote != 0) vote.toFloat() / 100 else 100f },
                                    color = circularProgressColor,
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
    Box(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp)
            .padding(bottom = 50.dp)
    ) {
        Column {
            movie.overview?.let {
                Text(
                    text = "Overview",
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = movie.overview.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 18.sp
                )
            }
        }
    }
}
