package com.pko.core.data.models.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieDetailResponseDto(
    val id: Int,
    @Json(name = "backdrop_path") val backdropPath: String? = null,
    val overview: String? = null,
    @Json(name = "poster_path") val posterPath: String? = null,
    @Json(name = "release_date") val releaseDate: String? = null,
    val title: String? = null,
    val genres: List<MovieGenreDto>? = null,
    val video: Boolean,
    @Json(name = "vote_average") val voteAverage: Double,
    @Json(name = "vote_count") val voteCount: Int,
    val runtime: Int? = null,
    val favorite: Boolean = false
)

fun MovieDetailResponseDto.toDomain() =
    com.pko.core.domain.models.MovieDetailResponse(
        id = id,
        backdropPath = backdropPath,
        overview = overview,
        posterPath = posterPath,
        releaseDate = releaseDate,
        title = title,
        genres = genres?.map { it.toDomain() },
        video = video,
        voteAverage = voteAverage,
        voteCount = voteCount,
        runtime = runtime,
        favorite = favorite
    )
