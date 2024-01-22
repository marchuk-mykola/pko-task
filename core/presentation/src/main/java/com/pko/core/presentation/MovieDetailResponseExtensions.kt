package com.pko.core.presentation

import com.pko.core.domain.models.MovieDetailResponse
import kotlin.math.roundToInt

fun MovieDetailResponse.getFormattedMovieDescription(): String {
    val year = releaseDate?.substring(0..3) ?: "Unknown"
    val hours = runtime?.div(60) ?: 0
    val minutes = runtime?.rem(60) ?: 0

    return "$year • ${hours}h ${minutes}m"
}

fun MovieDetailResponse.getFormattedRating(): String {
    return "${calculateRating()}/5"
}

fun MovieDetailResponse.calculateRating(): Float {
    val scaledRating = voteAverage / 2 // Convert to a 0-5 scale
    return (scaledRating * 2).roundToInt() / 2.0f // Round to nearest 0.5
}