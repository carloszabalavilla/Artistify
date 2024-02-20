package com.czabala.miproyecto.model.artist

data class Image(
    val height: Int,
    val url: String,
    val width: Int

) {
    constructor() : this(0, "", 0)
}