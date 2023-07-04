package com.example.playlistmaker.search.domain.api

interface SearchRepository {
    fun findTrack()
    fun saveTrack()
    fun showHistory()
}
