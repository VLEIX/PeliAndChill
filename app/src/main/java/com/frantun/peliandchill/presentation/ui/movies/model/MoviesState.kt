package com.frantun.peliandchill.presentation.ui.movies.model

import com.frantun.peliandchill.domain.model.Movie

sealed class MoviesState {
    object ShowLoading : MoviesState()
    object SelectedCategoryType : MoviesState()
    data class RetrievedMovies(val movies: List<Movie>) : MoviesState()
}
