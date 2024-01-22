package com.pko.core.data.dataSources.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pko.core.data.models.database.MovieId

@Database(entities = [MovieId::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieIdDao

}