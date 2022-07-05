package com.frantun.peliandchill.domain.model

data class MoviesResult(
    val movies: List<Movie>,
    val page: Int? = null
)
