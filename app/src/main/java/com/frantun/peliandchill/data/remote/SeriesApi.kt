package com.frantun.peliandchill.data.remote

import com.frantun.peliandchill.BuildConfig
import com.frantun.peliandchill.data.remote.dto.SeriesDto
import com.frantun.peliandchill.data.remote.dto.VideosDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SeriesApi {

    @GET("tv/popular")
    suspend fun getPopularSeries(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("page") page: Int = 1
    ): Response<SeriesDto>

    @GET("tv/top_rated")
    suspend fun getTopRatedSeries(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("page") page: Int = 1
    ): Response<SeriesDto>

    @GET("tv/{id}/videos")
    suspend fun getVideosFromSeries(
        @Path("id") seriesId: Int,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): Response<VideosDto>

    @GET("search/tv")
    suspend fun searchSeriesByName(
        @Query("query") name: String,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("page") page: Int = 1
    ): Response<SeriesDto>
}
