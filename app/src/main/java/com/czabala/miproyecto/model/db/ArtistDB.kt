package com.czabala.miproyecto.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.czabala.miproyecto.model.server.artist.ExternalUrls
import com.czabala.miproyecto.model.server.artist.Followers
import com.czabala.miproyecto.model.server.artist.Image

@Entity
data class ArtistDB(
    @PrimaryKey
    val id: String,
    val external_urls: ExternalUrls, //O se pone así o no funciona
    val followers: Followers,
    val genres: List<String>,
    val images: List<Image>,
    val name: String,
    val popularity: Int,
    val type: String,
    val uri: String
)
