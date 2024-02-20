package com.czabala.miproyecto.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.czabala.miproyecto.core.AuthManager
import com.czabala.miproyecto.core.FirestoreManager
import com.czabala.miproyecto.model.artist.Artist
import com.czabala.miproyecto.model.search.SearchResponse
import com.czabala.miproyecto.model.song.RemoteConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArtistViewModel : ViewModel() {
    private val firestore = FirestoreManager()
    val auth = AuthManager()

    private val _artistsList = MutableLiveData<List<Artist>>()
    val artistList: LiveData<List<Artist>> get() = _artistsList
    private val _artist = MutableLiveData<Artist>()
    val artist: LiveData<Artist> get() = _artist
    val artistChanged = MutableLiveData<Boolean>()

    init {
        auth.getCurrentUser()?.let { firestore.setUserId(it.uid) }
        viewModelScope.launch {
            try {
                getFirestoreArtistList()
            } catch (e: Exception) {
                println("Exception on init ViewModel: " + e.message)
            }
        }
    }

    private fun getStandardList() {
        viewModelScope.launch {
            try {
                val searchResponse: SearchResponse? =
                    RemoteConnection.spotifyService.searchArtists("a").body()

                val artistIds =
                    searchResponse?.artists?.items?.map { it.data.uri.substringAfterLast(":") }

                val response = withContext(Dispatchers.IO) {
                    RemoteConnection.spotifyService.getArtists(artistIds?.joinToString(",") ?: "")
                }

                if (response.isSuccessful) {
                    val remoteArtist = response.body()
                    remoteArtist?.artists?.forEach {
                        println(it.name)
                    }
                    setArtistsList(remoteArtist?.artists ?: emptyList())
                } else {
                    println("No se pudo obtener la lista de artistas")
                }
            } catch (e: Exception) {
                println("Excepcion lanzada: " + e.message)
            }
        }
    }

    private fun getFirestoreArtistList() {
        viewModelScope.launch {
            try {
                val response = firestore.getArtists()
                setArtistsList(response)
            } catch (e: Exception) {
                println("Excepcion lanzada al intentar conseguir artistas en la base de datos: " + e.message)
                e.printStackTrace()
            }
        }
    }

    fun setArtist(artist: Artist) {
        _artist.postValue(artist)
    }

    fun setArtistsList(artistsList: List<Artist>) {
        _artistsList.postValue(artistsList)
    }

    fun addArtistToArtistsList(artist: Artist) {
        _artistsList.value?.let {
            val newList = it.toMutableList()
            newList.add(artist)
            _artistsList.postValue(newList)
        }
        artistChanged.postValue(true)
    }

    fun removeArtist(artist: Artist) {
        _artistsList.value?.let {
            val newList = it.toMutableList()
            newList.remove(artist)
            _artistsList.postValue(newList)

        }
        artistChanged.postValue(true)
    }

    fun removeAllArtists() {
        _artistsList.postValue(emptyList())
        artistChanged.postValue(true)
    }

    suspend fun searchArtistById(artist: Artist, context: Context): Artist? {
        return firestore.getArtistById(artist.id)
    }

    suspend fun saveArtistOnFirestore(artist: Artist, context: Context): Boolean {
        firestore.addArtist(artist)
        return true
    }

    suspend fun modifyArtistOnFirestore(artist: Artist, context: Context) {
        firestore.updateArtist(artist)
    }

    suspend fun deleteArtistOnFirestore(artist: Artist, context: Context) {
        firestore.deleteArtistById(artist.id)
    }
}

