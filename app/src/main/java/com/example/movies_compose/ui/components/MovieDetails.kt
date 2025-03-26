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

/**
 * Composable que muestra la información principal de una película, incluyendo su imagen de fondo, título, tagline, géneros, fecha de estreno, duración y promedio de votos. También incluye un icono para marcar la película como favorita.
 *
 * @param movie Objeto `MovieDetail` que contiene la información detallada de la película.
 * @param isFavorite Estado booleano que indica si la película está marcada como favorita.
 * @param onFavoriteClick Función que se ejecuta cuando se hace clic en el icono de favorito.
 *
 * @see AsyncImage Muestra la imagen de fondo de la película.
 * @see CircularProgressIndicator Muestra el progreso de la calificación de la película.
 * @see Icon Muestra el icono de favorito.
 * @see ConstraintLayout Layout que organiza la información principal de forma relativa a la imagen.
 * @see Text Muestra textos como el título, fecha de estreno, género, etc.
 */
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
                        contentDescription = context.resources.getString(R.string.movie_backdrop_image),
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
                            contentDescription = context.resources.getString(R.string.favorite_icon),
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
                        fontSize = 45.sp
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
                                    text = if (vote != 0) "${vote}%" else context.resources.getString(R.string.no_vote_average),
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

/**
 * Composable que muestra el argumento de una película. Si la película tiene un resumen no vacío, se muestra un título seguido del texto del resumen en una caja con un fondo blanco.
 *
 * @param movie Objeto `MovieDetail` que contiene la información de la película, incluyendo su resumen.
 *
 * @see Text Muestra el título y el resumen de la película.
 * @see Column Organiza los elementos en una columna.
 * @see Spacer Añade espacio entre los elementos.
 * @see Box Contenedor que permite organizar los elementos en la pantalla con un fondo y relleno.
 */
@Composable
fun MovieOverview(movie: MovieDetail) {
    val context = LocalContext.current
    if (!movie.overview.isNullOrBlank()) {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 25.dp)
                .padding(bottom = 50.dp)
        ) {
            Column {
                Text(
                    text = context.resources.getString(R.string.overview_detail_movie_fragment),
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
