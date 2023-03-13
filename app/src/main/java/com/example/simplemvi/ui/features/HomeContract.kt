package com.example.simplemvi.ui.features

import com.example.simplemvi.data.Movie


class HomeContract {

    // View actions
    sealed class Event {
        object GetMoviesClick : Event()
        class MovieFavoriteClick(val id: Int) : Event()
    }

    // state for UI data
    data class State(
        val loading: Boolean = false,
        val data: List<Movie> = emptyList()
    )

    // one time effects handled in UI
    sealed class Effect {
        object ShowAddToFavorites : Effect()
        object NetworkError : Effect()
    }

}