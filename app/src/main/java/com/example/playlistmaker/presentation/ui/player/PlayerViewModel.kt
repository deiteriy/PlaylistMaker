package com.example.playlistmaker.presentation.ui.player

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.ViewModel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.MyApplication
import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.models.PlayerState
import java.util.Locale

class PlayerViewModel(
    private val trackId: Int,
    private val playerInteractor: PlayerInteractor,
) : ViewModel() {

    init {
        playerInteractor.setOnStateChangeListener { state ->
            stateLiveData.postValue(state)
            if (state == PlayerState.STATE_COMPLETE) handler.removeCallbacks(time)
        }
    }

    private val handler = Handler(Looper.getMainLooper())

    private val time = object : Runnable {
        override fun run() {
            val position = playerInteractor.getPosition()
            timeLiveData.postValue(SimpleDateFormat("mm:ss", Locale.getDefault()).format(position))
            handler.postDelayed(this, TRACK_TIME_DELAY)
        }
    }

    private val stateLiveData = MutableLiveData<PlayerState>()
    fun observeState(): LiveData<PlayerState> = stateLiveData

    private val timeLiveData = MutableLiveData<String>()
    fun observeTime(): LiveData<String> = timeLiveData

    fun prepare(url: String) {
        handler.removeCallbacks(time)
        playerInteractor.preparePlayer(url)
    }

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
        private const val TRACK_TIME_DELAY = 300L

        fun getViewModelFactory(trackId: Int): ViewModelProvider.Factory = viewModelFactory {
            // 2
            initializer {
                // 3
                val interactor = (this[APPLICATION_KEY] as MyApplication).providePlayerInteractor()

                PlayerViewModel(
                    trackId,
                    interactor,
                )
            }
        }
    }
}