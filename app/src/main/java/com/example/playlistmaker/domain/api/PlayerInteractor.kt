package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.PlayerState

interface PlayerInteractor {
    fun preparePlayer(url: String)
    fun startPlayer()
    fun pausePlayer()
    fun setOnStateChangeListener(callback: (PlayerState) -> Unit)

    fun getPosition()
    fun reset()

}