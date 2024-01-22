package com.pko.core.data.models.dto

import com.pko.core.domain.models.MovieSearchResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieSearchResponseDto(
    val results: List<MovieDto>,
    val page: Int,
    @Json(name = "total_pages")
    val totalPages: Int
)

fun MovieSearchResponseDto.toDomain() = MovieSearchResponse(
    results = results.map { it.toDomain() },
    page = page,
    totalPages = totalPages
)