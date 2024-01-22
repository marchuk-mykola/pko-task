package com.pko.core.domain.models

import java.io.Serializable

data class MovieGenre(
    val genresId: Int?,
    val name: String?
) : Serializable