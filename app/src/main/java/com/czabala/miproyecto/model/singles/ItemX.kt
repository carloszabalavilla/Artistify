package com.czabala.miproyecto.model.singles

data class ItemX(
    val coverArt: CoverArt,
    val date: Date,
    val id: String,
    val name: String,
    val playability: Playability,
    val sharingInfo: SharingInfo,
    val tracks: Tracks,
    val type: String,
    val uri: String
)