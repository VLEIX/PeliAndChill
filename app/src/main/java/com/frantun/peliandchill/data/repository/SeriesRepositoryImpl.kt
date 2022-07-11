package com.frantun.peliandchill.data.repository

import com.frantun.peliandchill.common.Constants.ERROR_UNEXPECTED
import com.frantun.peliandchill.common.Constants.PAGE_NEGATIVE
import com.frantun.peliandchill.common.Constants.PAGE_ONE
import com.frantun.peliandchill.common.Constants.TYPE_POPULAR
import com.frantun.peliandchill.common.Constants.TYPE_TOP_RATED
import com.frantun.peliandchill.common.Resource
import com.frantun.peliandchill.data.datasource.SeriesLocalDataSource
import com.frantun.peliandchill.data.datasource.SeriesRemoteDataSource
import com.frantun.peliandchill.data.remote.dto.SeriesDto
import com.frantun.peliandchill.data.remote.dto.VideosDto
import com.frantun.peliandchill.domain.model.SeriesResult
import com.frantun.peliandchill.domain.repository.SeriesRepository
import javax.inject.Inject

class SeriesRepositoryImpl @Inject constructor(
    private val seriesLocalDataSource: SeriesLocalDataSource,
    private val seriesRemoteDataSource: SeriesRemoteDataSource
) : SeriesRepository {

    override suspend fun getPopularSeries(page: Int): Resource<SeriesResult> {
        return try {
            when (val result = seriesRemoteDataSource.getPopularSeries(page)) {
                is Resource.Success -> {
                    getPopularSeriesResultSuccess(result.data)?.let { seriesResult ->
                        Resource.Success(seriesResult)
                    } ?: Resource.Error(ERROR_UNEXPECTED)
                }
                is Resource.Error -> {
                    val seriesLocal = seriesLocalDataSource.getPopularSeries()
                    val seriesResult = SeriesResult(seriesLocal, PAGE_NEGATIVE)
                    Resource.Success(seriesResult)
                }
                else -> Resource.Error(ERROR_UNEXPECTED)
            }
        } catch (exception: Exception) {
            Resource.Error(exception.message ?: exception.toString())
        }
    }

    private suspend fun getPopularSeriesResultSuccess(seriesDto: SeriesDto?): SeriesResult? {
        return seriesDto?.let {
            val seriesRemote = seriesDto.series.map { series ->
                series.apply {
                    type = TYPE_POPULAR
                }
            }
            if (seriesDto.page == PAGE_ONE) {
                seriesLocalDataSource.deletePopularSeries()
            }
            seriesLocalDataSource.insertSeries(seriesRemote)
            val seriesLocal = seriesLocalDataSource.getPopularSeries()
            SeriesResult(seriesLocal, seriesDto.page)
        }
    }

    override suspend fun getTopRatedSeries(page: Int): Resource<SeriesResult> {
        return try {
            when (val result = seriesRemoteDataSource.getTopRatedSeries(page)) {
                is Resource.Success -> {
                    getTopRatedSeriesResultSuccess(result.data)?.let { seriesResult ->
                        Resource.Success(seriesResult)
                    } ?: Resource.Error(ERROR_UNEXPECTED)
                }
                is Resource.Error -> {
                    val seriesLocal = seriesLocalDataSource.getTopRatedSeries()
                    val seriesResult = SeriesResult(seriesLocal, PAGE_NEGATIVE)
                    Resource.Success(seriesResult)
                }
                else -> Resource.Error(ERROR_UNEXPECTED)
            }
        } catch (exception: Exception) {
            Resource.Error(exception.message ?: exception.toString())
        }
    }

    private suspend fun getTopRatedSeriesResultSuccess(seriesDto: SeriesDto?): SeriesResult? {
        return seriesDto?.let {
            val seriesRemote = seriesDto.series.map { series ->
                series.apply {
                    type = TYPE_TOP_RATED
                }
            }
            if (seriesDto.page == PAGE_ONE) {
                seriesLocalDataSource.deleteTopRatedSeries()
            }
            seriesLocalDataSource.insertSeries(seriesRemote)
            val seriesLocal = seriesLocalDataSource.getTopRatedSeries()
            SeriesResult(seriesLocal, seriesDto.page)
        }
    }

    override suspend fun getVideosFromSeries(seriesId: String): Resource<VideosDto> {
        return seriesRemoteDataSource.getVideosFromSeries(seriesId = seriesId)
    }

    override suspend fun searchSeriesByName(name: String, page: Int): Resource<SeriesDto> {
        return seriesRemoteDataSource.searchSeriesByName(name = name, page = page)
    }
}
