package com.frantun.peliandchill.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.frantun.peliandchill.data.local.dao.MovieDao
import com.frantun.peliandchill.data.local.dao.SeriesDao
import com.frantun.peliandchill.domain.model.Movie
import com.frantun.peliandchill.domain.model.Series

@Database(
    entities = [Movie::class, Series::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun seriesDao(): SeriesDao
}
