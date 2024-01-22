package com.pko.core.data.models.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "moviesId")
data class MovieId(
    @PrimaryKey
    val id: Int
)