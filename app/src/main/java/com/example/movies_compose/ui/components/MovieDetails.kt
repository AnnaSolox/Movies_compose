package com.example.movies_compose.ui.components

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.example.movies_compose.R

@Composable
fun MovieMainInformation(modifier: Modifier = Modifier) {

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

    Column(
        modifier
            .fillMaxWidth()
            .height(screenHeightDp)
    ) {
        ConstraintLayout {
            val information = createRef()

            Box(Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = "Backdrop image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
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
                    Text(
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .weight(1f),
                        text = "(2024)",
                        style = MaterialTheme.typography.displaySmall
                    )
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "FavoriteIcon",
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.Top)
                    )
                }
                Text(
                    text = "Título de la película".uppercase(),
                    style = MaterialTheme.typography.displayMedium,
                )
                Text(
                    modifier = Modifier.padding(bottom = 25.dp),
                    text = "Tagline",
                    style = MaterialTheme.typography.displaySmall,
                    fontSize = 18.sp
                )
                Row(Modifier.fillMaxWidth()) {
                    Column(
                        Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Comedia, drama, suspense, terror, ficción, romance.",
                            style = MaterialTheme.typography.bodyLarge,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Release date",
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "Runtime",
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 18.sp
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        Box(
                            contentAlignment = Alignment.CenterEnd,
                            modifier = Modifier.size(55.dp)
                        ) {
                            CircularProgressIndicator(
                                progress = { .5f },
                                modifier = Modifier.fillMaxSize(),
                            )
                            Text(
                                text = "50%",
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

@Composable
fun MovieOverview() {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp)
            .padding(bottom = 50.dp)
    ) {
        Column {
            Text(
                text = "Overview",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Lorem ipsum dolor set amet",
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun MovieDetails(navController: NavHostController) {
    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())) {
        MovieMainInformation()
        MovieOverview()
    }
}
