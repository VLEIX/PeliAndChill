package com.frantun.peliandchill.presentation.ui.detail.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frantun.peliandchill.common.Resource
import com.frantun.peliandchill.domain.model.Movie
import com.frantun.peliandchill.domain.model.Video
import com.frantun.peliandchill.domain.usecase.GetVideosFromMovieUseCase
import com.frantun.peliandchill.presentation.ui.detail.movie.model.MovieDetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val getVideosFromMovieUseCase: GetVideosFromMovieUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<MovieDetailState?>(null)
    val state: StateFlow<MovieDetailState?> get() = _state

    lateinit var movie: Movie

    fun initState(movie: Movie) {
        this.movie = movie
        _state.value = MovieDetailState.Initialized(movie)
    }

    fun getVideos() {
        val resource = getVideosFromMovieUseCase(movie.id)

        resource.onEach { result ->
            when (result) {
                is Resource.Loading -> _state.value = MovieDetailState.ShowLoading
                is Resource.Success -> videosSuccess(result.data)
                is Resource.Error -> _state.value = MovieDetailState.ShowError
            }
        }.launchIn(viewModelScope)
    }

    private fun videosSuccess(videos: List<Video>?) {
        videos?.let {
            val videosWithPoster = it.map { video ->
                video.apply {
                    posterPath = movie.posterPath
                    backdropPath = movie.backdropPath
                }
            }
            _state.value = MovieDetailState.RetrievedVideos(videosWithPoster)
        }
    }
}
