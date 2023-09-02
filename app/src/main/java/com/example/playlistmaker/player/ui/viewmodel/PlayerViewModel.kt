package com.example.playlistmaker.player.ui.viewmodel

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.db.FavoritesInteractor
import com.example.playlistmaker.library.domain.db.PlaylistInteractor
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.library.ui.models.PlaylistState
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.models.PlayerState
import com.example.playlistmaker.player.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.Locale

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val favoritesInteractor: FavoritesInteractor,
    private val playlistInteractor: PlaylistInteractor,
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

    private val _playlistLiveData = MutableLiveData<List<Playlist>>()

    fun observePlaylistState(): LiveData<List<Playlist>> = _playlistLiveData


    fun initWithTrack(item: Track) {
        track = item
        viewModelScope.launch {
            track!!.isFavorite = isFavoriteTrack(track!!.trackId)
            isFavoriteLiveData.postValue(track!!.isFavorite)
        }
        playerInteractor.preparePlayer(track!!.previewUrl!!)
        playerInteractor.setOnStateChangeListener { state ->
            stateLiveData.postValue(state)
            if (state == PlayerState.STATE_COMPLETE) stopUpdatingTime()
        }
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

    fun showPlaylists() {
        viewModelScope.launch {
            playlistInteractor.loadPlaylists().collect() {
                _playlistLiveData.postValue(it)
            }
        }
    }

    companion object {
        private const val TRACK_TIME_DELAY = 300L
    }

    private suspend fun isFavoriteTrack(trackId: Long): Boolean {
        return favoritesInteractor.isFavorite(trackId)
    }
}