package com.pko.core.data.dataSources.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pko.core.data.models.database.MovieId
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieIdDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovieId(movieId: MovieId)

    @Query("DELETE FROM moviesId WHERE id = :movieId")
    suspend fun deleteMovie(movieId: Int)

    @Query("SELECT * FROM moviesId WHERE id = :movieId")
    fun getSingleMovie(movieId: Int): Flow<MovieId?>

    @Query("SELECT * FROM moviesId")
    fun getMovies(): Flow<List<MovieId>>

}

