package com.czabala.miproyecto.model

import com.czabala.miproyecto.BuildConfig
import com.czabala.miproyecto.core.DemoStore
import com.czabala.miproyecto.core.DemoResponses
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.Protocol
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RemoteConnection {
    private val client = OkHttpClient.Builder().addInterceptor { chain ->
        val request = chain.request()
        if (BuildConfig.DEMO_MODE) {
            val file = when (request.url.encodedPath) {
                "/artists/" -> "artists"
                "/tracks/" -> "tracks"
                "/artist_singles/" -> "singles"
                else -> if (request.url.queryParameter("type") == "tracks") "search-tracks" else "search-artists"
            }
            Response.Builder().request(request).protocol(Protocol.HTTP_1_1).code(200).message("Local demo")
                .body(DemoResponses.filter(DemoStore.fixture(file), file, request.url.queryParameter("q"), request.url.queryParameter("ids"))
                    .toResponseBody("application/json".toMediaType())).build()
        } else {
            check(BuildConfig.RAPIDAPI_KEY.isNotBlank()) { "Configure RAPIDAPI_KEY for the live build" }
            chain.proceed(request.newBuilder().addHeader("X-RapidAPI-Key", BuildConfig.RAPIDAPI_KEY)
                .addHeader("X-RapidAPI-Host", "spotify23.p.rapidapi.com").build())
        }
    }.build()
    val spotifyService: SpotifyApiService = Retrofit.Builder().baseUrl("https://spotify23.p.rapidapi.com/")
        .client(client).addConverterFactory(GsonConverterFactory.create()).build().create(SpotifyApiService::class.java)
}
