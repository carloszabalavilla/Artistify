package com.czabala.miproyecto.core

import android.util.Log
import com.czabala.miproyecto.model.artist.Artist
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreManager() {
    private val firestore = FirebaseFirestore.getInstance()
    private val collection = "artists"


    fun checkConnection(onSuccess: () -> Unit, onFailure: () -> Unit) {
        firestore.collection("yourCollection")
            .limit(1)
            .get()
            .addOnSuccessListener {
                Log.d("FirestoreManager", "Connection successful")
                onSuccess()
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreManager", "Connection failed", exception)
                onFailure()
            }
    }

    suspend fun getArtistById(artistId: String): Artist? {
        val artistRef = firestore.collection(collection).document(artistId).get().await()
        return artistRef.toObject(Artist::class.java)
    }

    suspend fun getArtistByName(artistName: String): Artist? {
        val artistRef = firestore.collection(collection).document(artistName).get().await()
        return artistRef.toObject(Artist::class.java)
    }

    suspend fun addArtist(artist: Artist) {
        firestore.collection(collection).add(artist).await()
    }

    suspend fun updateArtist(artist: Artist) {
        val artistRef = artist.id.let {
            firestore.collection(collection).document(it)
        }
        artistRef.set(artist).await()
    }

    suspend fun deleteArtistById(artistId: String) {
        firestore.collection(collection).document(artistId).delete().await()
    }
    /*
        fun getArtistsFlow(): Flow<List<Artist>> = callbackFlow {
            val artistsRef = firestore.collection(collection)
                .whereEqualTo("userId", userId)
                .orderBy("name")
            val subscription = artistsRef.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                snapshot?.let { querySnapshot ->
                    val artists = mutableListOf<Artist>()
                    for (document in querySnapshot.documents) {
                        val artist = document.toObject(Artist::class.java)
                        //artist?.id = document.id
                        artist?.let { artists.add(artist) }
                    }
                    trySend(artists).isSuccess
                }
            }
            awaitClose { subscription.remove() }
        }
     */
}