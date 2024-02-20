package com.czabala.miproyecto.model.track

data class TrackResponse(
    val tracks: List<Track>
) {
    constructor() : this(listOf())
}