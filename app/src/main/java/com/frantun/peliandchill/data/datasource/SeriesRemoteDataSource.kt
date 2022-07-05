package com.frantun.peliandchill.data.datasource

import com.frantun.peliandchill.common.Resource
import com.frantun.peliandchill.data.remote.dto.SeriesDto
import com.frantun.peliandchill.data.remote.dto.VideosDto

interface SeriesRemoteDataSource {
    suspend fun getTopRatedSeries(): Resource<SeriesDto>
    suspend fun getPopularSeries(): Resource<SeriesDto>
    suspend fun getVideosFromSeries(seriesId: Int): Resource<VideosDto>
    suspend fun searchSeriesByName(name: String): Resource<SeriesDto>
}
