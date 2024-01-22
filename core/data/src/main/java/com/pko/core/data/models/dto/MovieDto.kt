package com.pko.core.data.models.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieDto(
    val id: Int,
    @Json(name = "vote_count") var voteCount: Int,
    @Json(name = "poster_path") var posterPath: String? = null,
    @Json(name = "backdrop_path") var backdropPath: String? = null,
    var title: String,
    var overview: String? = null,
    @Json(name = "vote_average") var voteAverage: String? = null,
    @Json(name = "release_date") var releaseDate: String? = null,
    var runtime: Int? = null
)

fun MovieDto.toDomain() =
    com.pko.core.domain.models.Movie(
        id = id,
        voteCount = voteCount,
        posterPath = posterPath,
        backdropPath = backdropPath,
        title = title,
        overview = overview,
        voteAverage = voteAverage,
        releaseDate  = releaseDate,
        runtime = runtime
    )