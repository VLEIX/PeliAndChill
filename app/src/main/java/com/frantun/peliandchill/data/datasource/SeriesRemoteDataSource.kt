package com.frantun.peliandchill.data.datasource

import com.frantun.peliandchill.data.remote.dto.SeriesDto
import com.frantun.peliandchill.data.remote.dto.VideosDto

interface SeriesRemoteDataSource {
    suspend fun getTopRatedSeries(): SeriesDto
    suspend fun getPopularSeries(): SeriesDto
    suspend fun getVideosFromSeries(seriesId: Int): VideosDto
    suspend fun searchSeriesByName(name: String): SeriesDto
}
