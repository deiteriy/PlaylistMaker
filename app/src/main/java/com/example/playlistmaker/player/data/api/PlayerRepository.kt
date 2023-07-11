package com.example.playlistmaker.player.data.api

import com.example.playlistmaker.player.domain.models.PlayerState
import com.example.playlistmaker.player.domain.models.Track

interface PlayerRepository {
    fun preparePlayer(url: String)
    fun startPlayer()
    fun pausePlayer()
    fun setOnStateChangeListener(callback: (PlayerState) -> Unit)
    fun getPosition(): Long

    fun reset()
//    fun getTrack(): Track
}