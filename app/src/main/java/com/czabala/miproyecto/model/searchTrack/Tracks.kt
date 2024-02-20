package com.czabala.miproyecto.model.searchTrack

data class Tracks(
    val items: List<Item>,
    val pagingInfo: PagingInfo,
    val totalCount: Int
)