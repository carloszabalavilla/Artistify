package com.czabala.miproyecto.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.czabala.miproyecto.model.server.RemoteConnection
import com.czabala.miproyecto.model.server.artist.Artist
import com.czabala.miproyecto.model.server.search.SearchResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArtistViewModel : ViewModel() {
    private val _artistsList = MutableLiveData<List<Artist>>()
    val artistList: LiveData<List<Artist>> get() = _artistsList
    private val _artist = MutableLiveData<Artist>()
    val artist: LiveData<Artist> get() = _artist
    val artistChanged = MutableLiveData<Boolean>()


    init {
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

    fun setArtist(artist: Artist) {
        _artist.postValue(artist)
    }

    fun setArtistsList(artistsList: List<Artist>) {
        _artistsList.postValue(artistsList)
    }

    fun addArtistToArtistList(artist: Artist) {
        _artistsList.value?.let {
            val newList = it.toMutableList()
            newList.add(artist)
            _artistsList.postValue(newList)
        }
        artistChanged.postValue(true)
    }

    fun deleteArtist(artist: Artist) {
        _artistsList.value?.let {
            val newList = it.toMutableList()
            newList.remove(artist)
            _artistsList.postValue(newList)
        }
        artistChanged.postValue(true)
    }

    fun deleteAllArtists() {
        _artistsList.postValue(emptyList())
        artistChanged.postValue(true)
    }
}

