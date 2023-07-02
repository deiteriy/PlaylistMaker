package com.example.playlistmaker.player.data.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitApi {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<TrackResponse>

}