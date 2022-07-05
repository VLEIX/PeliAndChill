package com.frantun.peliandchill.presentation.ui.home.model

import com.frantun.peliandchill.domain.model.Movie

sealed class HomeState {
    object Loading : HomeState()
    object Init : HomeState()
    data class Initialized(val movies: List<Movie>) : HomeState()
}
