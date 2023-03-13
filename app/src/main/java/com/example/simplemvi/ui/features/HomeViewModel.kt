package com.example.simplemvi.ui.features

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplemvi.data.FakeRepository
import com.example.simplemvi.ui.features.HomeContract.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class HomeViewModel(private val repository: FakeRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(State())
    val uiState = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<Event>()
    val event = _event.asSharedFlow()

    private val _effect: Channel<Effect> = Channel()
    val effect = _effect.receiveAsFlow()

    init {
        subscribeEvents()
    }

    private fun subscribeEvents() {
        viewModelScope.launch {
            event.collect {
                handleEvent(it)
            }
        }
    }

    private fun handleEvent(event: Event) {

        when (event) {

            is Event.GetMoviesClick -> {
                viewModelScope.launch {
                    setState { copy(loading = true) }

                    repository.getMovies().fold(
                        onSuccess = { movies ->
                            setState { copy(loading = false, data = movies) }
                        },
                        onFailure = {
                            setState { copy(loading = false) }
                            setEffect { Effect.NetworkError }
                        }
                    )

                }
            }

            is Event.MovieFavoriteClick -> {
                viewModelScope.launch {
                    repository.setFavorite(id = event.id, favorite = true)
                    setState {
                        copy(data = data.map { movie ->
                            if (movie.id == event.id) movie.copy(favorite = !movie.favorite) else movie
                        })
                    }
                }
                setEffect { Effect.ShowAddToFavorites }
            }
        }
    }

    fun setEvent(event: Event) = viewModelScope.launch { _event.emit(event) }

    private fun setState(reduce: State.() -> State) {
        _uiState.value = uiState.value.reduce()
    }

    private fun setEffect(effect: () -> Effect) {
        viewModelScope.launch {
            _effect.send(effect())
        }
    }

}