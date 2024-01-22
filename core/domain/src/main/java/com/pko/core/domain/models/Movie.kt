package com.pko.core.domain.models

import java.io.Serializable

data class Movie(
    val id: Int,
    val voteCount: Int,
    val posterPath: String? = null,
    val backdropPath: String? = null,
    val title: String,
    val overview: String? = null,
    val voteAverage: String? = null,
    val releaseDate: String? = null,
    val runtime: Int? = null,
    val favorite: Boolean = false
) : Serializable {

    fun getFormattedMovieDescription(): String {
        val year = releaseDate?.substring(0, 4) ?: "Unknown"
        val hours = runtime?.div(60) ?: 0
        val minutes = runtime?.rem(60) ?: 0
        return "$year • ${hours}h ${minutes}m"
    }

    val rating: Float
        get() = voteAverage?.toFloat()?.div(2) ?: 0f

    val formattedRating: String
        get() = "${calculateRating()}/5"

    private fun calculateRating(): Float {
        val rawRating = voteAverage?.toFloatOrNull() ?: return 0f
        val scale = (rawRating / 2) // Convert to a 0-5 scale
        return (Math.round(scale * 2) / 2.0).toFloat() // Round to nearest 0.5
    }
}