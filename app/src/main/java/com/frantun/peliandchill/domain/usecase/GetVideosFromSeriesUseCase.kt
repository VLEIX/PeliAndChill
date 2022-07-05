package com.frantun.peliandchill.domain.usecase

import com.frantun.peliandchill.common.Constants.ERROR_UNEXPECTED
import com.frantun.peliandchill.common.Resource
import com.frantun.peliandchill.domain.model.Video
import com.frantun.peliandchill.domain.repository.SeriesRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetVideosFromSeriesUseCase @Inject constructor(
    private val seriesRepository: SeriesRepository
) {
    operator fun invoke(movieId: Int): Flow<Resource<List<Video>>> = flow {
        try {
            emit(Resource.Loading<List<Video>>())
            val videosResult = seriesRepository.getVideosFromSeries(movieId)
            videosResult.data?.let {
                emit(Resource.Success<List<Video>>(it.videos))
            } ?: emit(Resource.Error<List<Video>>(ERROR_UNEXPECTED))
        } catch (exception: Exception) {
            emit(Resource.Error<List<Video>>(exception.message ?: ERROR_UNEXPECTED))
        }
    }
}
