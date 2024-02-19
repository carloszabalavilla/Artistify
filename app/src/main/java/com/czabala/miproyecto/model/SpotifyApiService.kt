package com.czabala.miproyecto.model

import com.czabala.miproyecto.model.artist.ArtistResponse
import com.czabala.miproyecto.model.search.SearchResponse
import com.czabala.miproyecto.model.singles.SingleResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SpotifyApiService {
    @GET("/artists/")
    suspend fun getArtists(@Query("ids") ids: String): Response<ArtistResponse>

    @GET("/search/")
    suspend fun searchArtists(
        @Query("q") query: String,
        @Query("type") type: String = "artist",
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 20
    ): Response<SearchResponse>

    @GET("/artist_singles/")
    suspend fun getTopTracks(
        @Query("id") artistId: String,
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 5
    ): Response<SingleResponse>
}