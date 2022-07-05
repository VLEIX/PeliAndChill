package com.frantun.peliandchill.data.datasource

import com.frantun.peliandchill.data.remote.SeriesApi
import com.frantun.peliandchill.data.remote.dto.SeriesDto
import com.frantun.peliandchill.data.remote.dto.VideosDto
import javax.inject.Inject

class SeriesRemoteDataSourceImpl @Inject constructor(
    private val seriesApi: SeriesApi
) : SeriesRemoteDataSource {

    override suspend fun getTopRatedSeries(): SeriesDto {
        return seriesApi.getTopRatedSeries()
    }

    override suspend fun getPopularSeries(): SeriesDto {
        return seriesApi.getPopularSeries()
    }

    override suspend fun getVideosFromSeries(seriesId: Int): VideosDto {
        return seriesApi.getVideosFromSeries(seriesId)
    }

    override suspend fun searchSeriesByName(name: String): SeriesDto {
        return seriesApi.searchSeriesByName(name)
    }
}
