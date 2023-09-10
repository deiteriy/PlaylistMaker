package com.example.playlistmaker.player.ui.viewmodel

import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.db.FavoritesInteractor
import com.example.playlistmaker.library.domain.db.PlaylistInteractor
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.models.PlayerState
import com.example.playlistmaker.player.domain.models.Track
import com.example.playlistmaker.search.ui.SearchViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val favoritesInteractor: FavoritesInteractor,
    private val playlistInteractor: PlaylistInteractor,
) : ViewModel() {
    private var track: Track? = null

    private var timeJob: Job? = null
    private var isClickAllowed = true
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

    private suspend fun isFavoriteTrack(trackId: Long): Boolean {
        return favoritesInteractor.isFavorite(trackId)
    }

    fun isInPlaylist(playlist: Playlist, trackId: Long): Boolean {
        var result = false
        for(track in playlist.tracks) {
            if(track == trackId) result = true
        }
        return result
    }

    fun addToPlaylist(playlist: Playlist, track: Track) {
        viewModelScope.launch {
            playlistInteractor.saveTrack(playlist, track)
        }
    }

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewModelScope.launch {
                delay(SearchViewModel.CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    companion object {
        private const val TRACK_TIME_DELAY = 300L
    }
}