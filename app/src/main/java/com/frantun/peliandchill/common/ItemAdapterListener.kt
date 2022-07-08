package com.frantun.peliandchill.common

class ItemAdapterListener(val clickListener: (position: Int) -> Unit) {
    fun onClick(position: Int) = clickListener(position)
}
