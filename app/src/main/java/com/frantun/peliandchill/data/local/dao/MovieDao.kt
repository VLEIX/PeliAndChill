package com.frantun.peliandchill.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.frantun.peliandchill.domain.model.Movie

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovies(movies: List<Movie>)

    @Query("SELECT * FROM movies WHERE type = :type ORDER BY rowId")
    suspend fun getMoviesByType(type: String): List<Movie>

    @Query("DELETE FROM movies WHERE type = :type")
    suspend fun deleteMoviesByType(type: String)
}
