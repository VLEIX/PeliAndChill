package com.frantun.peliandchill.presentation.ui.detail.movie.model

import com.frantun.peliandchill.domain.model.Movie
import com.frantun.peliandchill.domain.model.Video

sealed class MovieDetailState {
    data class Initialized(val movie: Movie) : MovieDetailState()
    object ShowLoading : MovieDetailState()
    object ShowError : MovieDetailState()
    data class RetrievedVideos(val videos: List<Video>) : MovieDetailState()
}
