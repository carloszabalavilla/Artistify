package com.czabala.miproyecto.core

import android.util.Log
import com.czabala.miproyecto.model.artist.Artist
import com.czabala.miproyecto.model.track.Track
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

    fun addArtist(artist: Artist) {
        firestore.collection(artistCollection).document(artist.id).set(artist)
            .addOnSuccessListener {
                Log.d("FirestoreManager", "Artist saved with ID: ${artist.id}")
            }.addOnFailureListener { e ->
            Log.e("FirestoreManager", "Error saving artist", e)
        }
    }

    suspend fun updateArtist(artist: Artist) {
        val artistRef = artist.id.let {
            firestore.collection(artistCollection).document(it)
        }
        artistRef.set(artist).await()
    }

    suspend fun deleteArtistById(artistId: String) {
        try {
            Log.d("FirestoreManager", "Deleting artist with id: $artistId")
            firestore.collection(artistCollection).document(artistId).delete().await().let {
                Log.d("FirestoreManager", "Artist deleted.")
            }
        } catch (e: Exception) {
            Log.e("FirestoreManager", "Error deleting artist.", e)
        }
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

    suspend fun getSongById(songId: String): Track? {
        val songRef = firestore.collection(songCollection).document(songId).get().await()
        return songRef.toObject(Track::class.java)
    }

    suspend fun getSongByName(songName: String): Track? {
        val songRef = firestore.collection(songCollection).document(songName).get().await()
        return songRef.toObject(Track::class.java)
    }

    fun addSong(song: Track) {
        firestore.collection(songCollection).document(song.id).set(song)
            .addOnSuccessListener {
                Log.d("FirestoreManager", "Song saved with ID: ${song.id}")
            }.addOnFailureListener { e ->
                Log.e("FirestoreManager", "Error saving song", e)
            }    }

    suspend fun updateSong(song: Track) {
        val songRef = song.id.let {
            firestore.collection(songCollection).document(it)
        }
        songRef.set(song).await()
    }

    suspend fun deleteSongById(songId: String) {
        try {
            Log.d("FirestoreManager", "Deleting song with id: $songId")
            firestore.collection(songCollection).document(songId).delete().await().let {
                Log.d("FirestoreManager", "Song deleted.")
            }
        } catch (e: Exception) {
            Log.e("FirestoreManager", "Error deleting song.", e)
        }
    }

    suspend fun getSongs(): List<Track> {
        return try {
            val result = firestore.collection(songCollection).get().await()
            result.toObjects(Track::class.java)
        } catch (e: Exception) {
            Log.e("FirestoreManager", "Error getting songs.", e)
            emptyList()
        }
    }
}