package com.czabala.miproyecto.model.search

data class SearchResponse(
    val artists: Artists,
    val query: String
)