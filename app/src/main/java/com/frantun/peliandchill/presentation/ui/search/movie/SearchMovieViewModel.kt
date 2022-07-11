package com.frantun.peliandchill.presentation.ui.search.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frantun.peliandchill.common.Constants.PAGE_ZERO
import com.frantun.peliandchill.common.Resource
import com.frantun.peliandchill.data.remote.dto.MoviesDto
import com.frantun.peliandchill.domain.model.Movie
import com.frantun.peliandchill.domain.usecase.SearchMovieByNameUseCase
import com.frantun.peliandchill.presentation.ui.search.movie.model.SearchMovieState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
class SearchMovieViewModel @Inject constructor(
    private val searchMovieByNameUseCase: SearchMovieByNameUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<SearchMovieState?>(null)
    val state: StateFlow<SearchMovieState?> get() = _state

    val lastItemIndexFlow = MutableStateFlow<Int?>(null)
    val nameFlow = MutableStateFlow("")

    private var name = ""
    private var page: Int = PAGE_ZERO
    private var movies: List<Movie> = emptyList()

    init {
        viewModelScope.launch {
            nameFlow.collect {
                name = it
                page = PAGE_ZERO
                movies = emptyList()

                tryToGetMovies(0)
            }
        }

        viewModelScope.launch {
            lastItemIndexFlow.collect {
                it?.let {
                    tryToGetMovies(it)
                }
            }
        }
    }

    private fun tryToGetMovies(lastItemIndex: Int) {
        if (name.isEmpty()) {
            page = PAGE_ZERO
            movies = emptyList()
            _state.value = SearchMovieState.Initialized(movies)
        } else if (shouldGetMovies(lastItemIndex)) {
            getMovies(page)
        }
    }

    private fun shouldGetMovies(lastItemIndex: Int) =
        (page == PAGE_ZERO) || ((lastItemIndex + 1) >= movies.size)

    private fun getMovies(page: Int) {
        val nextPage = page + 1
        val resource = searchMovieByNameUseCase(name, nextPage)

        resource.onEach { result ->
            when (result) {
                is Resource.Loading -> _state.value = SearchMovieState.ShowLoading
                is Resource.Success -> moviesSuccess(result.data)
                is Resource.Error -> _state.value = SearchMovieState.ShowError
            }
        }.launchIn(viewModelScope)
    }

    private fun moviesSuccess(moviesDto: MoviesDto?) {
        moviesDto?.let {
            page = it.page
            movies = movies + it.movies
            _state.value = SearchMovieState.RetrievedMovies(movies)
        }
    }
}
