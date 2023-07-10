package com.example.playlistmaker.search.data.api

import com.example.playlistmaker.player.domain.models.Track
import com.example.playlistmaker.search.domain.NetworkError

interface NetworkClient {
    fun doRequest(request: String,
                  onSuccess: (List<Track>) -> Unit,
                  onError: (NetworkError) -> Unit)
}