package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.player.domain.models.Track

class TrackResponse(
    val resultCount: Int,
    val results: ArrayList<Track>) {
}