package com.pko.core.presentation

import com.pko.core.domain.models.Movie

sealed class RecyclerItem {
    data class MovieItem(val movie: Movie) : RecyclerItem()
    data object EmptyResult : RecyclerItem()
    data object Loading : RecyclerItem()
    data object Error : RecyclerItem()
}