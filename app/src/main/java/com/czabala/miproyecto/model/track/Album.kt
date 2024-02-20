package com.czabala.miproyecto.model.track

data class Album(
    val album_type: String,
    val artists: List<ArtistX>,
    val external_urls: ExternalUrlsXXX,
    val id: String,
    val images: List<Image>,
    val name: String,
    val release_date: String,
    val release_date_precision: String,
    val total_tracks: Int,
    val type: String,
    val uri: String
)   {
    constructor() : this(
        "",
        emptyList(),
        ExternalUrlsXXX(""),
        "",
        emptyList(),
        "",
        "",
        "",
        0,
        "",
        ""
    )
}