package com.pko.details.viewModel

import com.pko.core.domain.models.MovieDetailResponse

sealed class DetailState {
    data object Loading : DetailState()
    data class Success(val movieDetails: MovieDetailResponse) : DetailState()
    data class Error(val message: String) : DetailState()
}