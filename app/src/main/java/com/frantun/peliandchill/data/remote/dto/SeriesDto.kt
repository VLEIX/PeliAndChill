package com.frantun.peliandchill.data.remote.dto

import com.frantun.peliandchill.domain.model.Series
import com.google.gson.annotations.SerializedName

data class SeriesDto(
    val page: Int,
    @SerializedName("results")
    val series: List<Series>
)
