package com.frantun.peliandchill.data.remote.dto

import com.frantun.peliandchill.domain.model.Video
import com.google.gson.annotations.SerializedName

data class VideosDto(
    @SerializedName("results")
    val videos: List<Video>
)
