package com.pko.core.domain.models

data class MovieDetailResponse(
    val id: Int,
    val backdropPath: String? = null,
    val overview: String? = null,
    val posterPath: String? = null,
    val releaseDate: String? = null,
    val title: String? = null,
    val genres: List<MovieGenre>? = null,
    val video: Boolean,
    val voteAverage: Double,
    val voteCount: Int,
    val runtime: Int? = null,
    val favorite: Boolean = false
)