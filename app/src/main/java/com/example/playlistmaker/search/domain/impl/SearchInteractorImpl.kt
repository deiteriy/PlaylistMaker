package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.SearchInteractor
import com.example.playlistmaker.search.domain.api.SearchRepository

class SearchInteractorImpl(private val repository: SearchRepository): SearchInteractor {
    override fun findTrack() {
        repository.findTrack()
    }

    override fun saveTrack() {
        repository.saveTrack()
    }

    override fun showHistory() {
        repository.showHistory()
    }
}