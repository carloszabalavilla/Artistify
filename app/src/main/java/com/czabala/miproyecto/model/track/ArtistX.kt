package com.czabala.miproyecto.model.track

data class ArtistX(
    val external_urls: ExternalUrlsXXX,
    val id: String,
    val name: String,
    val type: String,
    val uri: String
) {
    constructor() : this(
        ExternalUrlsXXX(""),
        "",
        "",
        "",
        ""
    )
}