package com.frantun.peliandchill.common

import com.frantun.peliandchill.domain.model.Movie

class MovieAdapterListener(val clickListener: (movie: Movie) -> Unit) {
    fun onClick(movie: Movie) = clickListener(movie)
}
