package com.czabala.miproyecto.core

import com.czabala.miproyecto.App
import com.czabala.miproyecto.BuildConfig
import com.czabala.miproyecto.model.artist.Artist
import com.czabala.miproyecto.model.artist.ArtistResponse
import com.czabala.miproyecto.model.track.Track
import com.czabala.miproyecto.model.track.TrackResponse
import com.google.gson.Gson

object DemoStore {
    private val gson = Gson()
    private val prefs get() = App.instance.getSharedPreferences("demo-catalog", 0)
    fun fixture(name: String): String = App.instance.assets.open("demo/$name.json").bufferedReader().use { it.readText() }
        .replace("DEMO_IMAGE", "android.resource://" + BuildConfig.APPLICATION_ID + "/drawable/baseline_music_note_24")
    fun artists(): List<Artist> = gson.fromJson(prefs.getString("artists", null) ?: fixture("artists"), ArtistResponse::class.java).artists
    fun songs(): List<Track> = gson.fromJson(prefs.getString("tracks", null) ?: fixture("tracks"), TrackResponse::class.java).tracks
    fun putArtist(artist: Artist) { prefs.edit().putString("artists", gson.toJson(ArtistResponse(artists().filterNot { it.id == artist.id } + artist))).apply() }
    fun putSong(song: Track) { prefs.edit().putString("tracks", gson.toJson(TrackResponse(songs().filterNot { it.id == song.id } + song))).apply() }
    fun deleteArtist(id: String) { prefs.edit().putString("artists", gson.toJson(ArtistResponse(artists().filterNot { it.id == id }))).apply() }
    fun deleteSong(id: String) { prefs.edit().putString("tracks", gson.toJson(TrackResponse(songs().filterNot { it.id == id }))).apply() }
}
