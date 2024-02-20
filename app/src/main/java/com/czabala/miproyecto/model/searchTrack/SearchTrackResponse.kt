package com.czabala.miproyecto.model.searchTrack

data class SearchTrackResponse(
    val query: String,
    val tracks: Tracks
)