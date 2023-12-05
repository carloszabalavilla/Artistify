package com.czabala.miproyecto.model.server.search

data class SearchResponse(
    val artists: Artists,
    val query: String
)