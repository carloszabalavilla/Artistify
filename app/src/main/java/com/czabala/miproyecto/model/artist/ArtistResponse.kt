package com.czabala.miproyecto.model.artist

data class ArtistResponse(
    val artists: List<Artist>
) {
    constructor() : this(emptyList())
}