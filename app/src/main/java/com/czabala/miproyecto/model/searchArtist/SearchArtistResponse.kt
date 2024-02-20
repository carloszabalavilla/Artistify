package com.czabala.miproyecto.model.searchArtist

data class SearchArtistResponse(
    val artists: Artists,
    val query: String
)