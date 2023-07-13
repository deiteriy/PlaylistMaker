package com.example.playlistmaker.player.data.api

import com.example.playlistmaker.player.domain.models.PlayerState

interface PlayerRepository {
    fun preparePlayer(url: String)
    fun startPlayer()
    fun pausePlayer()
    fun setOnStateChangeListener(callback: (PlayerState) -> Unit)
    fun getPosition(): Long
    fun reset()
}