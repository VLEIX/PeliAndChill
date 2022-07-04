package com.frantun.peliandchill.data.repository

import com.frantun.peliandchill.data.remote.SeriesApi
import com.frantun.peliandchill.data.remote.dto.SeriesDto
import com.frantun.peliandchill.data.remote.dto.VideosDto
import com.frantun.peliandchill.domain.repository.SeriesRepository
import javax.inject.Inject

class SeriesRepositoryImpl @Inject constructor(
    private val seriesApi: SeriesApi
) : SeriesRepository {

    override suspend fun getTopRatedSeries(): SeriesDto {
        return seriesApi.getTopRatedSeries()
    }

    override suspend fun getPopularSeries(): SeriesDto {
        return seriesApi.getPopularSeries()
    }

    override suspend fun getVideosFromSeries(seriesId: Int): VideosDto {
        return seriesApi.getVideosFromSeries(seriesId = seriesId)
    }

    override suspend fun searchSeriesByName(name: String): SeriesDto {
        return seriesApi.searchSeriesByName(name = name)
    }
}
