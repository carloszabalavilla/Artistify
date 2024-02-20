package com.czabala.miproyecto.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.czabala.miproyecto.core.AuthManager
import com.czabala.miproyecto.core.FirestoreManager
import com.czabala.miproyecto.model.track.Track
import kotlinx.coroutines.launch

class SongViewModel : ViewModel() {
    private val firestore = FirestoreManager()
    private val auth = AuthManager()

    private val _songsList = MutableLiveData<List<Track>>()
    val songList: LiveData<List<Track>> get() = _songsList
    private val _song = MutableLiveData<Track>()
    val song: LiveData<Track> get() = _song
    val songChanged = MutableLiveData<Boolean>()

    init {
        auth.getCurrentUser()?.let { firestore.setUserId(it.uid) }
        viewModelScope.launch {
            try {
                getFirestoreSongList()
            } catch (e: Exception) {
                println("Exception on init ViewModel: " + e.message)
            }
        }
    }

    private fun getFirestoreSongList() {
        viewModelScope.launch {
            try {
                val response = firestore.getSongs()
                setSongsList(response)
            } catch (e: Exception) {
                println("Excepcion lanzada al intentar conseguir artistas en la base de datos: " + e.message)
                e.printStackTrace()
            }
        }
    }

    fun setSong(song: Track) {
        _song.postValue(song)
    }

    fun setSongsList(songsList: List<Track>) {
        _songsList.postValue(songsList)
    }

    fun addSongToSongsList(song: Track) {
        _songsList.value?.let {
            val newList = it.toMutableList()
            newList.add(song)
            _songsList.postValue(newList)
        }
        songChanged.postValue(true)
    }

    fun removeSong(song: Track) {
        _songsList.value?.let {
            val newList = it.toMutableList()
            newList.remove(song)
            _songsList.postValue(newList)

        }
        songChanged.postValue(true)
    }

    fun removeAllSongs() {
        _songsList.postValue(emptyList())
        songChanged.postValue(true)
    }

    suspend fun searchSongById(song: Track): Track? {
        return firestore.getSongById(song.id)
    }

    fun saveSongOnFirestore(song: Track): Boolean {
        firestore.addSong(song)
        return true
    }

    suspend fun modifySongOnFirestore(song: Track) {
        firestore.updateSong(song)
    }

    suspend fun deleteSongOnFirestore(song: Track) {
        firestore.deleteSongById(song.id)
    }
}

