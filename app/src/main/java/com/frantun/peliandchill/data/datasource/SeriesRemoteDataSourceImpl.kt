package com.frantun.peliandchill.data.datasource

import com.frantun.peliandchill.common.BaseRemoteDataSource
import com.frantun.peliandchill.common.Resource
import com.frantun.peliandchill.data.remote.SeriesApi
import com.frantun.peliandchill.data.remote.dto.SeriesDto
import com.frantun.peliandchill.data.remote.dto.VideosDto
import javax.inject.Inject

class SeriesRemoteDataSourceImpl @Inject constructor(
    private val seriesApi: SeriesApi
) : BaseRemoteDataSource(), SeriesRemoteDataSource {

    override suspend fun getTopRatedSeries(): Resource<SeriesDto> = getResult {
        seriesApi.getTopRatedSeries()
    }

    override suspend fun getPopularSeries(): Resource<SeriesDto> = getResult {
        seriesApi.getPopularSeries()
    }

    override suspend fun getVideosFromSeries(seriesId: Int): Resource<VideosDto> = getResult {
        seriesApi.getVideosFromSeries(seriesId)
    }

    override suspend fun searchSeriesByName(name: String): Resource<SeriesDto> = getResult {
        seriesApi.searchSeriesByName(name)
    }
}