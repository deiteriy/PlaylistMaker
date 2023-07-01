package com.example.playlistmaker.presentation.ui.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.Creator
import com.example.playlistmaker.domain.models.Track

class PlayerViewModelFactory(private val track: Track): ViewModelProvider.Factory {
    val interactor = Creator.providePlayerInteractor()

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PlayerViewModel(track, interactor) as T
    }
}