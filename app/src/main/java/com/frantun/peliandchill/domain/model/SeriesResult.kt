package com.frantun.peliandchill.domain.model

data class SeriesResult(
    val series: List<Series>,
    val page: Int? = null
)
