package com.example.playlistmaker.search.data.api

import com.example.playlistmaker.search.data.network.Response

interface NetworkClient {
    suspend fun doRequest(request: String): Response
}