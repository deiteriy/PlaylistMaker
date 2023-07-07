package com.example.playlistmaker.main.ui

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.main.domain.api.MainActivityInteractor

class MainViewModel(private val interactor: MainActivityInteractor): ViewModel() {

    fun openSearchActivity() {
        interactor.openSearchActivity()
    }

    fun openSettingsActivity() {
        interactor.openSettingsActivity()
    }

    fun openLibraryActivity() {
        interactor.openLibraryActivity()
    }
}