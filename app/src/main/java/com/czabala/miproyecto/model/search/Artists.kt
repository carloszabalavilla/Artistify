package com.czabala.miproyecto.model.search

data class Artists(
    val items: List<Item>,
    val pagingInfo: PagingInfo,
    val totalCount: Int
)