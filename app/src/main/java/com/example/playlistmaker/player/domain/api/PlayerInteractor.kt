package com.example.playlistmaker.player.domain.api

import com.example.playlistmaker.player.domain.models.PlayerState
import com.example.playlistmaker.player.domain.models.Track

interface PlayerInteractor {
    fun preparePlayer(url: String)
    fun startPlayer()
    fun pausePlayer()
    fun setOnStateChangeListener(callback: (PlayerState) -> Unit)
    fun getPosition(): Long
    fun reset()
  //  fun getTrack(): Track

}