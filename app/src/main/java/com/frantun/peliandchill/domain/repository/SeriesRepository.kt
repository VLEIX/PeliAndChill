package com.frantun.peliandchill.domain.repository

import com.frantun.peliandchill.data.remote.dto.SeriesDto
import com.frantun.peliandchill.data.remote.dto.VideosDto

interface SeriesRepository {
    suspend fun getTopRatedSeries(): SeriesDto
    suspend fun getPopularSeries(): SeriesDto
    suspend fun getVideosFromSeries(seriesId: Int): VideosDto
    suspend fun searchSeriesByName(name: String): SeriesDto
}
