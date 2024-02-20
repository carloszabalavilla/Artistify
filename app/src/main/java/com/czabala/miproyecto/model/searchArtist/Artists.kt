package com.czabala.miproyecto.model.searchArtist

data class Artists(
    val items: List<Item>,
    val pagingInfo: PagingInfo,
    val totalCount: Int
)