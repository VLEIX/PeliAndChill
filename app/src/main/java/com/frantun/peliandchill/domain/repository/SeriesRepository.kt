package com.frantun.peliandchill.domain.repository

import com.frantun.peliandchill.common.Resource
import com.frantun.peliandchill.data.remote.dto.SeriesDto
import com.frantun.peliandchill.data.remote.dto.VideosDto
import com.frantun.peliandchill.domain.model.SeriesResult

interface SeriesRepository {
    suspend fun getPopularSeries(): Resource<SeriesResult>
    suspend fun getTopRatedSeries(): Resource<SeriesResult>
    suspend fun getVideosFromSeries(seriesId: Int): Resource<VideosDto>
    suspend fun searchSeriesByName(name: String): Resource<SeriesDto>
}
