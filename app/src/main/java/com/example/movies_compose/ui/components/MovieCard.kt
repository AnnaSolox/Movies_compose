package com.example.movies_compose.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.movies_compose.R

@Preview(showBackground = true)
@Composable
fun MovieCard(modifier: Modifier = Modifier) {

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp

    val heightInDp = screenHeight * 0.6

    val brush = Brush.linearGradient(
        0.0f to Color.Black,
        .6f to Color.Transparent,
        start = Offset(0.0f, 1000.0f),
        end = Offset(0.0f, 000.0f)
    )
    ElevatedCard(
        modifier
            .fillMaxWidth()
            .height(heightInDp.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.elevatedCardElevation(16.dp),
    ) {
        ConstraintLayout {
            val (gradient, info) = createRefs()
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
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
                    Text(
                        text = "Nombre de la película",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )

                    Text(
                        text = "Fecha de lanzamiento",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                }

                Column(
                    Modifier.weight(.25f),
                    horizontalAlignment = Alignment.End
                ) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite icon",
                        Modifier.size(40.dp),
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun CardsScreen(modifier: Modifier){
    LazyColumn (modifier.fillMaxSize()) {
        items(4){
            MovieCard()
        }
    }
}