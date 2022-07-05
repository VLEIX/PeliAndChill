package com.frantun.peliandchill.data.datasource

import com.frantun.peliandchill.domain.model.Series

interface SeriesLocalDataSource {
    suspend fun insertSeries(series: List<Series>)
    suspend fun getPopularSeries(): List<Series>
    suspend fun getTopRatedSeries(): List<Series>
}
