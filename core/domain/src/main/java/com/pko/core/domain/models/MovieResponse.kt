package com.pko.core.domain.models

data class MovieResponse(
    val results: List<Movie>,
    val page: Int,
    val totalPages: Int
)