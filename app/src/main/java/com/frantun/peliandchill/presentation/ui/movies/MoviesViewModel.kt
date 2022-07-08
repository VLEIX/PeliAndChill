package com.frantun.peliandchill.presentation.ui.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frantun.peliandchill.common.Constants
import com.frantun.peliandchill.common.Constants.PAGE_NEGATIVE
import com.frantun.peliandchill.common.Constants.PAGE_ZERO
import com.frantun.peliandchill.common.Resource
import com.frantun.peliandchill.domain.model.Movie
import com.frantun.peliandchill.domain.model.MoviesResult
import com.frantun.peliandchill.domain.usecase.GetPopularMoviesUseCase
import com.frantun.peliandchill.domain.usecase.GetTopRatedMoviesUseCase
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
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<MoviesState?>(null)
    val state: StateFlow<MoviesState?> get() = _state

    val lastItemIndexFlow = MutableStateFlow<Int?>(null)
    val categoryTypeFlow = MutableStateFlow(Constants.CategoryType.TYPE_POPULAR)

    private var categoryType = Constants.CategoryType.TYPE_POPULAR
    private var page: Int = PAGE_ZERO
    private var movies: List<Movie> = emptyList()

    init {
        viewModelScope.launch {
            categoryTypeFlow.collect {
                categoryType = it
                page = PAGE_ZERO

                _state.value = MoviesState.SelectedCategoryType
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
        if (shouldGetMovies(lastItemIndex)) {
            getMovies(page)
        }
    }

    private fun shouldGetMovies(lastItemIndex: Int) =
        (page == PAGE_ZERO) || ((lastItemIndex + 1) >= movies.size)

    private fun getMovies(page: Int) {
        if (page != PAGE_NEGATIVE) {
            val nextPage = page + 1
            val resource = when (categoryType) {
                Constants.CategoryType.TYPE_POPULAR -> getPopularMoviesUseCase(nextPage)
                Constants.CategoryType.TYPE_TOP_RATED -> getTopRatedMoviesUseCase(nextPage)
            }

            resource.onEach { result ->
                when (result) {
                    is Resource.Loading -> _state.value = MoviesState.ShowLoading
                    is Resource.Success -> moviesSuccess(result.data)
                    is Resource.Error -> Unit
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun moviesSuccess(moviesResult: MoviesResult?) {
        moviesResult?.let {
            page = it.page
            movies = it.movies
            _state.value = MoviesState.RetrievedMovies(movies)
        }
    }
}
