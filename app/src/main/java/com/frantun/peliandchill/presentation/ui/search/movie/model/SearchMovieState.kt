package com.frantun.peliandchill.presentation.ui.search.movie.model

import com.frantun.peliandchill.domain.model.Movie

sealed class SearchMovieState {
    data class Initialized(val movies: List<Movie>) : SearchMovieState()
    object ShowLoading : SearchMovieState()
    object ShowError : SearchMovieState()
    data class RetrievedMovies(val movies: List<Movie>) : SearchMovieState()
}
