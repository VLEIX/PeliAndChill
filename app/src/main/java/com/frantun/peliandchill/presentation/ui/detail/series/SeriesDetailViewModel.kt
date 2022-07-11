package com.frantun.peliandchill.presentation.ui.detail.series

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frantun.peliandchill.common.Resource
import com.frantun.peliandchill.domain.model.Series
import com.frantun.peliandchill.domain.model.Video
import com.frantun.peliandchill.domain.usecase.GetVideosFromSeriesUseCase
import com.frantun.peliandchill.presentation.ui.detail.series.model.SeriesDetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@HiltViewModel
class SeriesDetailViewModel @Inject constructor(
    private val getVideosFromSeriesUseCase: GetVideosFromSeriesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<SeriesDetailState?>(null)
    val state: StateFlow<SeriesDetailState?> get() = _state

    lateinit var series: Series

    fun initState(series: Series) {
        this.series = series
        _state.value = SeriesDetailState.Initialized(series)
    }

    fun getVideos() {
        val resource = getVideosFromSeriesUseCase(series.id)

        resource.onEach { result ->
            when (result) {
                is Resource.Loading -> _state.value = SeriesDetailState.ShowLoading
                is Resource.Success -> videosSuccess(result.data)
                is Resource.Error -> _state.value = SeriesDetailState.ShowError
            }
        }.launchIn(viewModelScope)
    }

    private fun videosSuccess(videos: List<Video>?) {
        videos?.let {
            val videosWithPoster = it.map { video ->
                video.apply {
                    posterPath = series.posterPath
                    backdropPath = series.backdropPath
                }
            }
            _state.value = SeriesDetailState.RetrievedVideos(videosWithPoster)
        }
    }
}
