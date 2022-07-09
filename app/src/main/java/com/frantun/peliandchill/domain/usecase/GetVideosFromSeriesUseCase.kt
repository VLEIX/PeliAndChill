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
    operator fun invoke(movieId: String): Flow<Resource<List<Video>>> = flow {
        try {
            emit(Resource.Loading())
            val videosResult = seriesRepository.getVideosFromSeries(movieId)
            videosResult.data?.let {
                emit(Resource.Success(it.videos))
            } ?: emit(Resource.Error(ERROR_UNEXPECTED))
        } catch (exception: Exception) {
            emit(Resource.Error(exception.message ?: ERROR_UNEXPECTED))
        }
    }
}
