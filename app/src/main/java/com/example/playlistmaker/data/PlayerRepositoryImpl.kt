package com.example.playlistmaker.data

import android.media.MediaPlayer
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.PlayerRepository
import com.example.playlistmaker.domain.models.PlayerState
import com.example.playlistmaker.presentation.ui.player.PlayerActivity

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