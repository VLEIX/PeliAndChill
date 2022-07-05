package com.frantun.peliandchill.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frantun.peliandchill.common.Resource
import com.frantun.peliandchill.domain.usecase.GetPopularMoviesUseCase
import com.frantun.peliandchill.presentation.ui.home.model.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<HomeState?>(null)
    val state: StateFlow<HomeState?> get() = _state

    init {
        getPopularMovies()
    }

    fun getPopularMovies() {
        getPopularMoviesUseCase().onEach { result ->
            when (result) {
                is Resource.Loading -> println()
                is Resource.Success -> _state.value = HomeState.Initialized(result.data!!.movies)
                is Resource.Error -> println(result.message)
            }
        }.launchIn(viewModelScope)
    }
}
