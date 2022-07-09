package com.frantun.peliandchill.common

import com.frantun.peliandchill.domain.model.Video

class VideoAdapterListener(val clickListener: (video: Video) -> Unit) {
    fun onClick(video: Video) = clickListener(video)
}
