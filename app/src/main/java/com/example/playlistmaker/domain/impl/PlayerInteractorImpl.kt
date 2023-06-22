package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.api.PlayerRepository

class PlayerInteractorImpl(private val repository: PlayerRepository): PlayerInteractor {
    override fun preparePlayer(url: String) {
        repository.preparePlayer(url)
    }

    override fun startPlayer() {
        repository.startPlayer()
    }

    override fun pausePlayer() {
        repository.pausePlayer()
    }

    override fun playbackControl() {
        repository.playbackControl()
    }
}