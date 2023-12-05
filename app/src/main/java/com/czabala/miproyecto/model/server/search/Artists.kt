package com.czabala.miproyecto.model.server.search

data class Artists(
    val items: List<Item>,
    val pagingInfo: PagingInfo,
    val totalCount: Int
)