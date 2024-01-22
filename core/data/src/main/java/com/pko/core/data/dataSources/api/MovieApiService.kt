package com.pko.core.data.dataSources.api

import com.pko.core.data.models.dto.MovieDetailResponseDto
import com.pko.core.data.models.dto.MovieResponseDto
import com.pko.core.data.models.dto.MovieSearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(@Query("page") page: Int): MovieResponseDto

    @GET("movie/{id}")
    suspend fun getMovieDetails(@Path("id") movieId: Int): MovieDetailResponseDto

    @GET("search/movie")
    suspend fun searchMovies(@Query("query") search: String, @Query("page") page: Int): MovieSearchResponseDto

}