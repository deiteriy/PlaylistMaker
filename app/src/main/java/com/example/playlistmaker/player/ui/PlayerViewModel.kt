package com.example.playlistmaker.player.ui

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.models.PlayerState
import com.example.playlistmaker.player.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
) : ViewModel() {
    private var track: Track? = null

    private var timeJob: Job? = null
    private suspend fun updateTime() {
        while (true) {
            val position = playerInteractor.getPosition()
            timeLiveData.postValue(SimpleDateFormat("mm:ss", Locale.getDefault()).format(position))
            delay(TRACK_TIME_DELAY)
        }
    }

    private fun startUpdatingTime() {
        timeJob = viewModelScope.launch {
            updateTime()
        }
    }

    private fun stopUpdatingTime() {
        timeJob?.cancel()
    }

    private val stateLiveData = MutableLiveData<PlayerState>()

    private val timeLiveData = MutableLiveData<String>()


    fun initWithTrack(item: Track) {

        track = item
        playerInteractor.preparePlayer(track!!.previewUrl)
        playerInteractor.setOnStateChangeListener { state ->
            stateLiveData.postValue(state)
            if (state == PlayerState.STATE_COMPLETE) stopUpdatingTime()
        }
    }

    fun observeState(): LiveData<PlayerState> = stateLiveData

    fun observeTime(): LiveData<String> = timeLiveData

    fun play() {
        playerInteractor.startPlayer()
        startUpdatingTime()
    }

    fun pause() {
        playerInteractor.pausePlayer()
        stopUpdatingTime()
    }

    fun release() {
        playerInteractor.reset()
        stopUpdatingTime()
    }

    companion object {
        private const val TRACK_TIME_DELAY = 1000L
    }
}