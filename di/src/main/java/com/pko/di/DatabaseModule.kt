package com.pko.di

import android.content.Context
import androidx.room.Room
import com.pko.core.data.dataSources.database.MovieDatabase
import com.pko.core.data.dataSources.database.MovieIdDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Provides
    @Singleton
    fun provideMovieDatabase(
        @ApplicationContext applicationContext: Context,
    ): MovieDatabase {
        return Room
            .databaseBuilder(applicationContext, MovieDatabase::class.java, "Movie Database")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideMovieIdDao(database: MovieDatabase): MovieIdDao {
        return database.movieDao()
    }

}