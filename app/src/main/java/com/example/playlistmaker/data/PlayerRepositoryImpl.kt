package com.example.playlistmaker.data

import android.media.MediaPlayer
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.PlayerRepository
import com.example.playlistmaker.presentation.ui.player.PlayerActivity

class PlayerRepositoryImpl(private val mediaPlayer: MediaPlayer): PlayerRepository {

    override fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            binding.playButton.isEnabled = true
            playerState = PlayerActivity.STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = PlayerActivity.STATE_PREPARED
            binding.trackProgress.text = "00:00"
            binding.playButton.setImageResource(R.drawable.play_button)
        }
    }

    override fun startPlayer() {
        if(playerState != PlayerActivity.STATE_PLAYING){
            mediaPlayer.start()
            binding.playButton.setImageResource(R.drawable.pause_button)
            playerState = PlayerActivity.STATE_PLAYING
            startTimer()
        }
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        binding.playButton.setImageResource(R.drawable.play_button)
        playerState = PlayerActivity.STATE_PAUSED
        handler.removeCallbacksAndMessages(null)
    }

    override fun playbackControl() {
        when (playerState) {
            PlayerActivity.STATE_PLAYING -> {
                pausePlayer()
            }
            PlayerActivity.STATE_PREPARED, PlayerActivity.STATE_PAUSED -> {
                startPlayer()
            }
        }
    }
}