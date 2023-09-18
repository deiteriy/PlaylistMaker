package com.example.playlistmaker.player.data.impl

import android.media.MediaPlayer
import android.util.Log
import com.example.playlistmaker.player.data.api.PlayerRepository
import com.example.playlistmaker.player.domain.models.PlayerState

class PlayerRepositoryImpl(private val mediaPlayer: MediaPlayer): PlayerRepository {

    private var stateCallback: ((PlayerState) -> Unit)? = null
    override fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            stateCallback?.invoke(PlayerState.STATE_PREPARED)
        }
        mediaPlayer.setOnCompletionListener {
            stateCallback?.invoke(PlayerState.STATE_COMPLETE)
        }

    }

    override fun setPosition(position: Int) {
        mediaPlayer.setOnPreparedListener { mp ->
            mediaPlayer.seekTo(position)
        }


    }

    override fun startPlayer() {
        mediaPlayer.start()
        stateCallback?.invoke(PlayerState.STATE_PLAYING)
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        stateCallback?.invoke(PlayerState.STATE_PAUSED)
    }

    override fun getPosition() = mediaPlayer.currentPosition.toLong()

    override fun setOnStateChangeListener(callback: (PlayerState) -> Unit) {
        stateCallback = callback
    }

    override fun reset() {
        mediaPlayer.reset()
    }
}