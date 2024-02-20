package com.czabala.miproyecto.core

import android.util.Log
import com.czabala.miproyecto.model.artist.Artist
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreManager {
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private lateinit var userId: String
    private lateinit var artistCollection: String
    private lateinit var songCollection: String


    fun setUserId(userId: String) {
        this.userId = userId
        artistCollection = "users/$userId/artists"
        songCollection = "users/$userId/songs"
    }

    suspend fun getArtistById(artistId: String): Artist? {
        val artistRef = firestore.collection(artistCollection).document(artistId).get().await()
        return artistRef.toObject(Artist::class.java)
    }

    suspend fun getArtistByName(artistName: String): Artist? {
        val artistRef = firestore.collection(artistCollection).document(artistName).get().await()
        return artistRef.toObject(Artist::class.java)
    }

    suspend fun addArtist(artist: Artist) {
        firestore.collection(artistCollection).add(artist).await()
    }

    suspend fun updateArtist(artist: Artist) {
        val artistRef = artist.id.let {
            firestore.collection(artistCollection).document(it)
        }
        artistRef.set(artist).await()
    }

    suspend fun deleteArtistById(artistId: String) {
        firestore.collection(artistCollection).document(artistId).delete().await()
    }

    suspend fun getArtists(): List<Artist> {
        return try {
            val result = firestore.collection(artistCollection).get().await()
            result.toObjects(Artist::class.java)
        } catch (e: Exception) {
            Log.e("FirestoreManager", "Error getting artists.", e)
            emptyList()
        }
    }
}