package com.pko.core.data.repositories

import com.pko.core.data.dataSources.api.MovieApiService
import com.pko.core.data.dataSources.database.MovieIdDao
import com.pko.core.data.models.database.MovieId
import com.pko.core.data.models.dto.MovieDetailResponseDto
import com.pko.core.data.models.dto.toDomain
import com.pko.core.domain.models.MovieDetailResponse
import com.pko.core.domain.models.MovieResponse
import com.pko.core.domain.models.MovieSearchResponse
import com.pko.core.domain.repositories.IMovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class MovieRepository(
    private val api: MovieApiService,
    private val dao: MovieIdDao
) : IMovieRepository {

    override suspend fun getNowPlayingMovies(page: Int): Result<MovieResponse> {
        return try {
            Result.success(api.getNowPlayingMovies(page).toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchMovies(search: String, page: Int): Result<MovieSearchResponse> {
        return try {
            Result.success(api.searchMovies(search, page).toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMovieDetailsFlow(movieId: Int): Flow<Result<MovieDetailResponse>> {
        return flow {
            val result = try {
                val movieDetails = getMovieDetails(movieId)
                    .getOrThrow()

                Result.success(movieDetails.toDomain())
            } catch (e: Exception) {
                Result.failure(e)
            }

            emit(result)
        }.combine(
            dao.getSingleMovie(movieId)
        ) { movieDetailsResult, movieIdEntity ->
            movieDetailsResult.map { movieDetails ->
                movieDetails.copy(favorite = movieIdEntity != null)
            }
        }
    }

    override suspend fun addToFavorites(movieId: Int) {
        dao.insertMovieId(MovieId(movieId))
    }

    override suspend fun removeFromFavorites(movieId: Int) {
        dao.deleteMovie(movieId)
    }

    override suspend fun favoriteMovieIdsAsFlow(): Flow<List<Int>> {
        return dao.getMovies().map {
            it.map { movieId -> movieId.id }
        }
    }

    private suspend fun getMovieDetails(movieId: Int): Result<MovieDetailResponseDto> {
        return try {
            Result.success(api.getMovieDetails(movieId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}