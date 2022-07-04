package com.frantun.peliandchill.data.remote.dto

import com.frantun.peliandchill.domain.model.Movie
import com.google.gson.annotations.SerializedName

data class MoviesDto(
    val page: Int,
    @SerializedName("results")
    val movies: List<Movie>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)
