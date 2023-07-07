package com.example.playlistmaker.main.domain.impl

import com.example.playlistmaker.main.domain.api.MainActivityInteractor
import com.example.playlistmaker.main.domain.api.MainActivityRepository

class MainActivityInteractorImpl(private val repository: MainActivityRepository): MainActivityInteractor {
    override fun openSearchActivity() {
        repository.openSearchActivity()
    }

    override fun openSettingsActivity() {
        repository.openSettingsActivity()
    }

    override fun openLibraryActivity() {
        repository.openLibraryActivity()
    }


}
