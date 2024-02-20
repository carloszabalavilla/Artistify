package com.czabala.miproyecto.model.track

data class Track(
    val album: Album,
    val artists: List<ArtistX>,
    val disc_number: Int,
    val duration_ms: Int,
    val explicit: Boolean,
    val external_ids: ExternalIds,
    val external_urls: ExternalUrlsXXX,
    val id: String,
    val is_local: Boolean,
    val is_playable: Boolean,
    val name: String,
    val popularity: Int,
    val preview_url: String,
    val track_number: Int,
    val type: String,
    val uri: String
) {
    constructor() : this(
        Album(),
        emptyList(),
        0,
        0,
        false,
        ExternalIds(),
        ExternalUrlsXXX(""),
        "",
        false,
        false,
        "",
        0,
        "",
        0,
        "",
        ""
    )
}
