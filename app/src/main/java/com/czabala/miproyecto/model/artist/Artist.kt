package com.czabala.miproyecto.model.artist


data class Artist(
    //@get:PropertyName("external_urls") @set:PropertyName("external_urls")
    val external_urls: ExternalUrls, //O se pone así o no funciona
    val followers: Followers,
    val genres: List<String>,
    val id: String,
    val images: List<Image>,
    val name: String,
    val popularity: Int,
    val type: String,
    val uri: String
)
