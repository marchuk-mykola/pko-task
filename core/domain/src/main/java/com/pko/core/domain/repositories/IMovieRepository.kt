package com.pko.core.domain.repositories

import com.pko.core.domain.models.MovieDetailResponse
import com.pko.core.domain.models.MovieResponse
import com.pko.core.domain.models.MovieSearchResponse
import kotlinx.coroutines.flow.Flow

interface IMovieRepository {

    suspend fun getMovieDetailsFlow(movieId: Int): Flow<Result<MovieDetailResponse>>
    suspend fun getNowPlayingMovies(page: Int): Result<MovieResponse>
    suspend fun searchMovies(search: String, page: Int): Result<MovieSearchResponse>

    suspend fun addToFavorites(movieId: Int)
    suspend fun removeFromFavorites(movieId: Int)

    suspend fun favoriteMovieIdsAsFlow(): Flow<List<Int>>

}