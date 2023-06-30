package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.api.PlayerRepository
import com.example.playlistmaker.domain.models.PlayerState

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

    override fun setOnStateChangeListener(callback: (PlayerState) -> Unit) {
        repository.setOnStateChangeListener(callback)
    }

    override fun getPosition() {
        repository.getPosition()
    }

    override fun reset() {
        repository.reset()
    }
}