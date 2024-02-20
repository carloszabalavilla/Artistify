package com.czabala.miproyecto.model.artist


data class Artist(
    val external_urls: ExternalUrls,
    val followers: Followers,
    val genres: List<String>,
    val id: String,
    val images: List<Image>,
    val name: String,
    val popularity: Int,
    val type: String,
    val uri: String
) {
    constructor() : this(
        ExternalUrls(""),
        Followers("", 0),
        emptyList(),
        "",
        emptyList(),
        "",
        0,
        "",
        ""
    )
}
