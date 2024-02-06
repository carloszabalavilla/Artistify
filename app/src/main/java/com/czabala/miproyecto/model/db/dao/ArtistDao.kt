package com.czabala.miproyecto.model.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.Query
import com.czabala.miproyecto.model.db.ArtistDB

@Dao
interface ArtistDao {

    @Query("SELECT * FROM ArtistDB")
    fun getAll(): List<ArtistDB>

    @Query("SELECT * FROM ArtistDB WHERE id = :id")
    fun getById(id: String): ArtistDB

    @Query("SELECT * FROM ArtistDB WHERE name = :name")
    fun getByName(name: String): ArtistDB

    @Query("SELECT COUNT(*) FROM ArtistDB")
    fun count(): Int

    @Insert(onConflict = IGNORE)
    fun insert(artist: ArtistDB)

    @Insert(onConflict = IGNORE)
    fun insertAll(artists: List<ArtistDB>)
}