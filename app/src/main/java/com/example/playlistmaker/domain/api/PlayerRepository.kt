package com.example.playlistmaker.domain.api

interface PlayerRepository {
    fun preparePlayer(url: String)
    fun startPlayer()
    fun pausePlayer()
    fun playbackControl()
}