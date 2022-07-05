package com.frantun.peliandchill.presentation.ui.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frantun.peliandchill.common.Constants.PAGE_NEGATIVE
import com.frantun.peliandchill.common.Resource
import com.frantun.peliandchill.domain.model.Movie
import com.frantun.peliandchill.domain.model.MoviesResult
import com.frantun.peliandchill.domain.usecase.GetPopularMoviesUseCase
import com.frantun.peliandchill.presentation.ui.movies.model.MoviesState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<MoviesState?>(null)
    val state: StateFlow<MoviesState?> get() = _state

    val lastVisible = MutableStateFlow(0)

    private var page: Int = 0
    private var movies: List<Movie> = emptyList()

    init {
        viewModelScope.launch {
            lastVisible.collect {
                if (shouldCallGetPopularMovies(it)) {
                    getPopularMovies(page)
                }
            }
        }
    }

    private fun shouldCallGetPopularMovies(lastItemIndex: Int) = (lastItemIndex + 1) >= movies.size

    private fun getPopularMovies(page: Int) {
        if (page != PAGE_NEGATIVE) {
            getPopularMoviesUseCase(page + 1).onEach { result ->
                when (result) {
                    is Resource.Loading -> Unit
                    is Resource.Success -> popularMoviesSuccess(result.data)
                    is Resource.Error -> Unit
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun popularMoviesSuccess(moviesResult: MoviesResult?) {
        moviesResult?.let {
            page = it.page
            movies = it.movies
            _state.value = MoviesState.RetrievedMovies(it.movies)
        }
    }
}
