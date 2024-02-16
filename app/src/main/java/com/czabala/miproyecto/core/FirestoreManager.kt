package com.czabala.miproyecto.core

import android.content.Context
import com.czabala.miproyecto.App
import com.czabala.miproyecto.model.server.artist.Artist
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
class FirestoreManager(context: Context) {
    val firestore = FirebaseFirestore.getInstance()
    val auth = (context.applicationContext as App).auth
    val userId = auth.getCurrentUser()?.uid
    val COLLECTION = "artists"

    suspend fun getArtistById(artistId: String): Artist? {
        val artistRef = firestore.collection(COLLECTION).document(artistId).get().await()
        return artistRef.toObject(Artist::class.java)
    }

    suspend fun addArtist(artist: Artist){
        firestore.collection(COLLECTION).add(artist).await()
    }

    suspend fun updateNote(artist: Artist) {
        val artistRef = artist.id?.let {
            firestore.collection(COLLECTION).document(it)
        }
        artistRef?.set(artist)?.await()
    }

    suspend fun deleteNoteById(artistId: String) {
        firestore.collection(COLLECTION).document(artistId).delete().await()
    }
/*
    fun getNotesFlow(): Flow<List<Note>> = callbackFlow {
        val notesRef = firestore.collection("notes")
            .whereEqualTo("userId", userId)
            .orderBy("title")
        val subscription = notesRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            snapshot?.let{ QuerySnapshot ->
                val notes = mutableListOf<Note>()
                for (document in QuerySnapshot.documents) {
                    val note = document.toObject(Note::class.java)
                    note?.id = document.id
                    note?.let { notes.add(note) }
                }
                trySend(notes ?: emptyList()).isSuccess
            }
        }
        awaitClose { subscription.remove() }
    }
 */
}