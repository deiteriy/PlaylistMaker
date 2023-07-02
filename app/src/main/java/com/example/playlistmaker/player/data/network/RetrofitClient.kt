package com.example.playlistmaker.player.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val client: Retrofit by lazy {
        Retrofit.Builder()
        .baseUrl("https://itunes.apple.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    }
    val api: RetrofitApi by lazy {
        client.create(RetrofitApi::class.java)
    }
}