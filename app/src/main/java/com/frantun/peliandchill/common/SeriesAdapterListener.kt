package com.frantun.peliandchill.common

import com.frantun.peliandchill.domain.model.Series

class SeriesAdapterListener(val clickListener: (series: Series) -> Unit) {
    fun onClick(series: Series) = clickListener(series)
}
