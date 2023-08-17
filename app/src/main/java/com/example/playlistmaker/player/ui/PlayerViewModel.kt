package com.example.playlistmaker.player.ui

import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.db.FavoritesInteractor
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.models.PlayerState
import com.example.playlistmaker.player.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val favoritesInteractor: FavoritesInteractor,
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

    fun onFavoriteClicked() {
        if (track!!.isFavorite) {
            viewModelScope.launch {
                favoritesInteractor.deleteFromFavorite(track!!)
            }
            track!!.isFavorite = false
        } else {
            viewModelScope.launch {
                favoritesInteractor.markAsFavorite(track!!)
            }
            track!!.isFavorite = true
        }
        isFavoriteLiveData.postValue(track!!.isFavorite)
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

    private val isFavoriteLiveData = MutableLiveData<Boolean>()


    fun initWithTrack(item: Track) {

        track = item
        playerInteractor.preparePlayer(track!!.previewUrl)
        playerInteractor.setOnStateChangeListener { state ->
            stateLiveData.postValue(state)
            if (state == PlayerState.STATE_COMPLETE) stopUpdatingTime()
        }
        isFavoriteLiveData.postValue(track!!.isFavorite)
    }

    fun observeState(): LiveData<PlayerState> = stateLiveData

    fun observeTime(): LiveData<String> = timeLiveData

    fun observeIsFavorite(): LiveData<Boolean> = isFavoriteLiveData

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
        private const val TRACK_TIME_DELAY = 300L
    }
}