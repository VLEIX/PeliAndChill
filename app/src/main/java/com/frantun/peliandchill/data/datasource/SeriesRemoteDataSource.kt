package com.frantun.peliandchill.data.datasource

import com.frantun.peliandchill.common.Resource
import com.frantun.peliandchill.data.remote.dto.SeriesDto
import com.frantun.peliandchill.data.remote.dto.VideosDto

interface SeriesRemoteDataSource {
    suspend fun getTopRatedSeries(page: Int): Resource<SeriesDto>
    suspend fun getPopularSeries(page: Int): Resource<SeriesDto>
    suspend fun getVideosFromSeries(seriesId: String): Resource<VideosDto>
    suspend fun searchSeriesByName(name: String, page: Int): Resource<SeriesDto>
}
