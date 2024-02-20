package com.czabala.miproyecto.model

import com.czabala.miproyecto.model.artist.ArtistResponse
import com.czabala.miproyecto.model.searchArtist.SearchArtistResponse
import com.czabala.miproyecto.model.searchTrack.SearchTrackResponse
import com.czabala.miproyecto.model.singles.SingleResponse
import com.czabala.miproyecto.model.track.TrackResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SpotifyApiService {
    @GET("/artists/")
    suspend fun getArtists(@Query("ids") ids: String): Response<ArtistResponse>

    @GET("/search/")
    suspend fun searchArtists(
        @Query("q") query: String,
        @Query("type") type: String = "artists",
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 20
    ): Response<SearchArtistResponse>

    @GET("/search/")
    suspend fun searchArtist(
        @Query("q") query: String,
        @Query("type") type: String = "artists",
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 1
    ): Response<SearchArtistResponse>

    @GET("/artist_singles/")
    suspend fun getTopTracks(
        @Query("id") artistId: String,
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 5
    ): Response<SingleResponse>

    @GET("/tracks/")
    suspend fun getTracks(@Query("ids") ids: String): Response<TrackResponse>

    @GET("/search/")
    suspend fun searchTracks(
        @Query("q") query: String,
        @Query("type") type: String = "tracks",
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 20
    ): Response<SearchTrackResponse>

    @GET("/search/")
    suspend fun searchTrack(
        @Query("q") query: String,
        @Query("type") type: String = "tracks",
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 1
    ): Response<SearchTrackResponse>
}