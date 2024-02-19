package com.czabala.miproyecto.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.czabala.miproyecto.App
import com.czabala.miproyecto.model.RemoteConnection
import com.czabala.miproyecto.model.artist.Artist
import com.czabala.miproyecto.model.search.SearchResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/*
Introducir el firestoreManager para manejar directamente un artista desde el viewmodel, a la vez que lo anade en tiempo real lo envia a la base de datos.
Mas tarde cuando se suspenda el fragment debe recuperar lo que tiene de la base de datos(si hiciera falta)
 */

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

    //retornaria true si la respuesta es correcta? especificar los tipos de repuesta
    // que podria dar la funcion que llama a firestore, no solo respuesta correcta
    // e incorrecta
    // Pasar de item a artist

    suspend fun searchArtistById(artist: Artist, context: Context): Artist? {
        return (context.applicationContext as App).firestore.getArtistById(artist.id)
    }
    /*
    suspend fun getArtistList(artistList : List<Artist>,context: Context){
        var baseList : MutableList<Artist>
        for (artist in artistList){
            searchArtistById(artist,context)?.let { baseList.add(it) }
        }
    }
*/

    suspend fun saveArtist(artist: Artist, context: Context): Boolean {
        (context.applicationContext as App).firestore.addArtist(artist)
        return true
    }

    suspend fun alterArtist(artist: Artist, context: Context) {
        (context.applicationContext as App).firestore.updateArtist(artist)
    }

    suspend fun dropArtist(artist: Artist, context: Context) {
        (context.applicationContext as App).firestore.deleteArtistById(artist.id)
    }
}

