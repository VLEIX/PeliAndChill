package com.frantun.peliandchill.presentation.ui.movies.model

import com.frantun.peliandchill.common.Constants
import com.frantun.peliandchill.domain.model.Movie

sealed class MoviesState {
    object ShowLoading : MoviesState()
    data class SelectedCategoryType(val categoryType: Constants.CategoryType) : MoviesState()
    data class RetrievedMovies(val movies: List<Movie>) : MoviesState()
}
