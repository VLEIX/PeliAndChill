package com.frantun.peliandchill.presentation.ui.detail.series.model

import com.frantun.peliandchill.domain.model.Series
import com.frantun.peliandchill.domain.model.Video

sealed class SeriesDetailState {
    data class Initialized(val Series: Series) : SeriesDetailState()
    object ShowLoading : SeriesDetailState()
    object ShowError : SeriesDetailState()
    data class RetrievedVideos(val videos: List<Video>) : SeriesDetailState()
}
