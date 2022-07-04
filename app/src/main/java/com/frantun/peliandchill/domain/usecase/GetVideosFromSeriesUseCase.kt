package com.frantun.peliandchill.domain.usecase

import com.frantun.peliandchill.common.Constants.ERROR_CONNECTION
import com.frantun.peliandchill.common.Constants.ERROR_UNEXPECTED
import com.frantun.peliandchill.common.Resource
import com.frantun.peliandchill.domain.model.Video
import com.frantun.peliandchill.domain.repository.SeriesRepository
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class GetVideosFromSeriesUseCase @Inject constructor(
    private val seriesRepository: SeriesRepository
) {
    operator fun invoke(seriesId: Int): Flow<Resource<List<Video>>> = flow {
        try {
            emit(Resource.Loading<List<Video>>())
            val videos = seriesRepository.getVideosFromSeries(seriesId).videos
            emit(Resource.Success<List<Video>>(videos))
        } catch (e: HttpException) {
            emit(Resource.Error<List<Video>>(e.localizedMessage ?: ERROR_UNEXPECTED))
        } catch (e: IOException) {
            emit(Resource.Error<List<Video>>(ERROR_CONNECTION))
        }
    }
}
