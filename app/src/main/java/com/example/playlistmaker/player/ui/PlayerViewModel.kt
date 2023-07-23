package com.example.playlistmaker.player.ui

import android.icu.text.SimpleDateFormat
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.models.PlayerState
import com.example.playlistmaker.player.domain.models.Track
import java.util.Locale

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
) : ViewModel() {
    private var track: Track? = null

    private val handler = Handler(Looper.getMainLooper())

    private val time = object : Runnable {
        override fun run() {
            val position = playerInteractor.getPosition()
            timeLiveData.postValue(SimpleDateFormat("mm:ss", Locale.getDefault()).format(position))
            handler.postDelayed(this, TRACK_TIME_DELAY)
        }
    }

    private val stateLiveData = MutableLiveData<PlayerState>()

    private val timeLiveData = MutableLiveData<String>()


    fun initWithTrack(item: Track) {

        track = item
        playerInteractor.preparePlayer(track!!.previewUrl)
        playerInteractor.setOnStateChangeListener { state ->
            stateLiveData.postValue(state)
            if (state == PlayerState.STATE_COMPLETE) handler.removeCallbacks(time)
        }
    }

    fun observeState(): LiveData<PlayerState> = stateLiveData

    fun observeTime(): LiveData<String> = timeLiveData

    fun play() {
        playerInteractor.startPlayer()
        handler.post(time)
    }

    fun pause() {
        playerInteractor.pausePlayer()
        handler.removeCallbacks(time)
    }

    fun release() {
        playerInteractor.reset()
        handler.removeCallbacks(time)
    }

    companion object {
        private const val TRACK_TIME_DELAY = 1000L
    }
}