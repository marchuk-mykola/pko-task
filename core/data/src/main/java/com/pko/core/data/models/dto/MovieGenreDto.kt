package com.pko.core.data.models.dto

import com.pko.core.domain.models.MovieGenre
import com.squareup.moshi.JsonClass
import java.io.Serializable
@JsonClass(generateAdapter = true)
data class MovieGenreDto(
   val genresId: Int?,
   val name: String?
)

fun MovieGenreDto.toDomain() = MovieGenre(
    genresId = genresId,
    name = name
)