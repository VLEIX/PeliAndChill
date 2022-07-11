package com.frantun.peliandchill.data.repository

import com.frantun.peliandchill.common.Resource
import com.frantun.peliandchill.data.remote.dto.SeriesDto
import com.frantun.peliandchill.data.remote.dto.VideosDto
import com.frantun.peliandchill.domain.model.Series
import com.frantun.peliandchill.domain.model.SeriesResult
import com.frantun.peliandchill.domain.model.Video
import com.frantun.peliandchill.domain.repository.SeriesRepository

class FakeSeriesRepositoryImpl(
    private val series: List<Series>,
    private val videos: List<Video>
) : SeriesRepository {
    override suspend fun getPopularSeries(page: Int): Resource<SeriesResult> {
        return Resource.Success(SeriesResult(series, page))
    }

    override suspend fun getTopRatedSeries(page: Int): Resource<SeriesResult> {
        return Resource.Success(SeriesResult(series, page))
    }

    override suspend fun getVideosFromSeries(seriesId: String): Resource<VideosDto> {
        return Resource.Success(VideosDto(videos))
    }

    override suspend fun searchSeriesByName(name: String, page: Int): Resource<SeriesDto> {
        return Resource.Success(SeriesDto(page, series))
    }
}
