package com.pko.core.domain.models

data class MovieSearchResponse(
    val results: List<Movie>,
    val page: Int,
    val totalPages: Int
)