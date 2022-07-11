package com.frantun.peliandchill.common

import com.frantun.peliandchill.common.Constants.TYPE_POPULAR
import com.frantun.peliandchill.common.Constants.TYPE_TOP_RATED
import com.frantun.peliandchill.domain.model.Movie
import com.frantun.peliandchill.domain.model.Series
import com.frantun.peliandchill.domain.model.Video

fun moviesPopular() = listOf(moviePopular1(), moviePopular2())

fun moviesTopRated() = listOf(movieTopRated1(), movieTopRated2())

fun seriesPopular() = listOf(seriesPopular1(), seriesPopular2())

fun seriesTopRated() = listOf(seriesTopRated1(), seriesTopRated2())

fun videos() = listOf(video1(), video2())

fun moviePopular1() = Movie(
    "1", "", false, "", "", "", "", "", "", 0.0, 0, true, 0.0, TYPE_POPULAR
)

fun moviePopular2() = Movie(
    "2", "", false, "", "", "", "", "", "", 0.0, 0, true, 0.0, TYPE_POPULAR
)

fun movieTopRated1() = Movie(
    "3", "", false, "", "", "", "", "", "", 0.0, 0, true, 0.0, TYPE_TOP_RATED
)

fun movieTopRated2() = Movie(
    "4", "", false, "", "", "", "", "", "", 0.0, 0, true, 0.0, TYPE_TOP_RATED
)

fun seriesPopular1() = Series(
    "1", "", 0.0, "", 0.0, "", "", "", 0, "", "", TYPE_POPULAR
)

fun seriesPopular2() = Series(
    "2", "", 0.0, "", 0.0, "", "", "", 0, "", "", TYPE_POPULAR
)

fun seriesTopRated1() = Series(
    "3", "", 0.0, "", 0.0, "", "", "", 0, "", "", TYPE_TOP_RATED
)

fun seriesTopRated2() = Series(
    "4", "", 0.0, "", 0.0, "", "", "", 0, "", "", TYPE_TOP_RATED
)

fun video1() = Video(
    "1", "", "", "", "", "", 0, "", true, "", "", ""
)

fun video2() = Video(
    "2", "", "", "", "", "", 0, "", true, "", "", ""
)
