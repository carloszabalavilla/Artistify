package com.czabala.miproyecto

import com.czabala.miproyecto.model.artist.ArtistResponse
import com.czabala.miproyecto.model.track.TrackResponse
import com.czabala.miproyecto.model.searchArtist.SearchArtistResponse
import com.czabala.miproyecto.model.searchTrack.SearchTrackResponse
import com.google.gson.Gson
import org.junit.Test
import org.junit.Assert.*
import java.io.File
import com.czabala.miproyecto.core.DemoResponses

class DemoFixturesTest {
    private fun fixture(name: String) = File("src/main/assets/demo/$name.json").readText()
    @Test fun catalogContainsCompleteFictionalEntriesForTheExistingScreens() {
        val artists = Gson().fromJson(fixture("artists"), ArtistResponse::class.java).artists
        val songs = Gson().fromJson(fixture("tracks"), TrackResponse::class.java).tracks
        assertTrue(artists.size >= 3)
        assertEquals(artists.size, artists.map { it.id }.distinct().size)
        artists.forEach { assertTrue(it.name.isNotBlank()); assertTrue(it.images.isNotEmpty()); assertEquals("DEMO_IMAGE", it.images.first().url) }
        songs.forEach { assertTrue(it.album.images.isNotEmpty()); assertTrue(it.artists.isNotEmpty()); assertTrue(it.duration_ms > 0); assertTrue(it.is_local) }
    }
    @Test fun searchResultsReferToIdsAvailableInTheOfflineCatalog() {
        val artists = Gson().fromJson(fixture("artists"), ArtistResponse::class.java).artists.map { it.uri }
        val songs = Gson().fromJson(fixture("tracks"), TrackResponse::class.java).tracks.map { it.uri }
        val artistSearch = Gson().fromJson(fixture("search-artists"), SearchArtistResponse::class.java)
        val trackSearch = Gson().fromJson(fixture("search-tracks"), SearchTrackResponse::class.java)
        artistSearch.artists.items.forEach { assertTrue(artists.contains(it.data.uri)) }
        trackSearch.tracks.items.forEach { assertTrue(songs.contains(it.data.uri)) }
    }
    @Test fun offlineSearchFiltersByNameAndIdsAndCanReturnNoMatches() {
        val search = DemoResponses.filter(fixture("search-artists"), "search-artists", "local trio", null)
        val found = Gson().fromJson(search, SearchArtistResponse::class.java)
        assertEquals(1, found.artists.items.size)
        assertEquals("spotify:artist:demo-artist-2", found.artists.items.first().data.uri)
        val catalog = Gson().fromJson(DemoResponses.filter(fixture("artists"), "artists", null, "demo-artist-2"), ArtistResponse::class.java)
        assertEquals("Local Trio", catalog.artists.single().name)
        val empty = Gson().fromJson(DemoResponses.filter(fixture("search-tracks"), "search-tracks", "no-such-track", null), SearchTrackResponse::class.java)
        assertTrue(empty.tracks.items.isEmpty())
    }
}
