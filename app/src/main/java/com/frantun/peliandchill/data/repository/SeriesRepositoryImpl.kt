package com.frantun.peliandchill.data.repository

import com.frantun.peliandchill.data.datasource.SeriesLocalDataSource
import com.frantun.peliandchill.data.datasource.SeriesRemoteDataSource
import com.frantun.peliandchill.data.remote.dto.SeriesDto
import com.frantun.peliandchill.data.remote.dto.VideosDto
import com.frantun.peliandchill.domain.repository.SeriesRepository
import javax.inject.Inject

class SeriesRepositoryImpl @Inject constructor(
    private val seriesLocalDataSource: SeriesLocalDataSource,
    private val seriesRemoteDataSource: SeriesRemoteDataSource
) : SeriesRepository {

    override suspend fun getTopRatedSeries(): SeriesDto {
        return seriesRemoteDataSource.getTopRatedSeries()
    }

    override suspend fun getPopularSeries(): SeriesDto {
        return seriesRemoteDataSource.getPopularSeries()
    }

    override suspend fun getVideosFromSeries(seriesId: Int): VideosDto {
        return seriesRemoteDataSource.getVideosFromSeries(seriesId = seriesId)
    }

    override suspend fun searchSeriesByName(name: String): SeriesDto {
        return seriesRemoteDataSource.searchSeriesByName(name = name)
    }
}
