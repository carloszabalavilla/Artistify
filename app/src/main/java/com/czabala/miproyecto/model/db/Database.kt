package com.czabala.miproyecto.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.czabala.miproyecto.model.db.dao.ArtistDao
import com.czabala.miproyecto.model.server.artist.Artist

@Database(
        entities = [Artist::class],
        version = 1
    )
    abstract class ArtistDatabase: RoomDatabase(){
        abstract fun artistDao(): ArtistDao
    }
