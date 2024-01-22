package com.pko.details.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pko.core.domain.repositories.IMovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: IMovieRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState: MutableStateFlow<DetailState> = MutableStateFlow(DetailState.Loading)
    val uiState: StateFlow<DetailState> = _uiState.asStateFlow()

    fun getMovieDetails(movieId: Int) {
        _uiState.value = DetailState.Loading

        viewModelScope.launch(dispatcher) {
            repository.getMovieDetailsFlow(movieId)
                .collect { result ->
                    result
                        .onSuccess {
                            _uiState.value = DetailState.Success(it)
                        }
                        .onFailure {
                            _uiState.value = DetailState.Error(it.message.toString())
                        }
                }
        }
    }

    fun onHeartClicked(movieId: Int) {
        val currentState = _uiState.value

        if (currentState is DetailState.Success) {
            if (currentState.movieDetails.favorite) {
                removeFromFavorites(movieId)
            } else {
                addToFavorites(movieId)
            }
        }
    }

    private fun addToFavorites(movieId: Int) {
        viewModelScope.launch(dispatcher) {
            repository
                .addToFavorites(movieId)
        }
    }

    private fun removeFromFavorites(movieId: Int) {
        viewModelScope.launch(dispatcher) {
            repository
                .removeFromFavorites(movieId)
        }
    }

}