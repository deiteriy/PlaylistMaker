package com.example.playlistmaker.search.data.local

import com.example.playlistmaker.player.domain.models.Track

interface SearchHistory {
    fun read(): List<Track>
    fun write(track: Track)
    fun clear()
}