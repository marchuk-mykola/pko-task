package com.pko.di

import com.pko.core.data.dataSources.api.MovieApiService
import com.pko.core.data.dataSources.database.MovieIdDao
import com.pko.core.data.repositories.MovieRepository
import com.pko.core.domain.repositories.IMovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object RepositoriesModule {

    @Provides
    @Singleton
    fun provideMovieRepository(
        api: MovieApiService,
        dao: MovieIdDao
    ): IMovieRepository {
        return MovieRepository(
            api = api,
            dao = dao
        )
    }

}