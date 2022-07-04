package com.frantun.peliandchill.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.frantun.peliandchill.domain.model.Series

@Dao
interface SeriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeries(movies: List<Series>)

    @Query("SELECT * FROM series WHERE type=:type")
    suspend fun getSeriesByType(type: String): List<Series>
}
