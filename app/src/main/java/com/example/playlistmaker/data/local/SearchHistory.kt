package com.example.playlistmaker.data.local

import android.content.SharedPreferences
import com.example.playlistmaker.domain.models.Track

interface SearchHistory {
    fun read(): List<Track>
    fun write(track: Track)
    fun clear()
}