package com.czabala.miproyecto.model.server

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RemoteConnection {
        private const val BASE_URL = "https://spotify23.p.rapidapi.com/"

        private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("X-RapidAPI-Key", "ad2c6846c7mshda07506537f3797p1a0824jsn0a4b55c4a034")//c9ad19f8d2msh106e30b0f704818p1d9a30jsne42b208148da
                    .addHeader("X-RapidAPI-Host", "spotify23.p.rapidapi.com")
                    .build()
                chain.proceed(request)
            }
            .build()

        private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val spotifyService: SpotifyApiService = retrofit.create(SpotifyApiService::class.java)

}