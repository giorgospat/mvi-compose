package com.example.simplemvi.ui.features

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.simplemvi.R
import com.example.simplemvi.data.Movie
import com.example.simplemvi.ui.features.HomeContract.Event
import com.example.simplemvi.ui.features.HomeContract.State

@Composable
internal fun HomeScreen(viewModel: HomeViewModel) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)

    // Effects
    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.effect.collect { effect ->

                when (effect) {
                    is HomeContract.Effect.ShowAddToFavorites -> {
                        Toast.makeText(
                            context,
                            context.getString(R.string.add_favorite_message),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is HomeContract.Effect.NetworkError -> {
                        Toast.makeText(
                            context,
                            context.getString(R.string.network_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    // Layout
    Content(state = state, onEvent = viewModel::setEvent)

}

@Composable
private fun Content(
    state: State,
    onEvent: (Event) -> Unit
) {

    Box(modifier = Modifier.fillMaxSize()) {

        if (state.loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        Column {

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.best_movies_title),
                style = MaterialTheme.typography.h4,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = { onEvent(Event.GetMoviesClick) },
                enabled = !state.loading,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = stringResource(R.string.load_movies),
                    style = MaterialTheme.typography.button
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn {
                items(
                    items = state.data,
                    key = { movie: Movie -> movie.id }
                ) { movie ->
                    MovieItem(movie = movie) {
                        onEvent(Event.MovieFavoriteClick(id = movie.id))
                    }
                }
            }

        }
    }

}

@Composable
private fun MovieItem(movie: Movie, addToFavorite: () -> Unit) {

    Row(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Column {
            Text(text = movie.name, style = MaterialTheme.typography.body1)
            Text(text = movie.year.toString(), style = MaterialTheme.typography.body2)
        }

        IconButton(onClick = {
            addToFavorite()
        }) {
            Icon(
                imageVector = if (movie.favorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = stringResource(R.string.add_favorite_button)
            )
        }

    }

}