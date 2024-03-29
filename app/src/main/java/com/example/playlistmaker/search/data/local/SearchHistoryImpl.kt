package com.example.playlistmaker.search.data.local

import android.content.SharedPreferences
import android.util.Log
import com.example.playlistmaker.player.domain.models.Track
import com.example.playlistmaker.search.data.TrackMapper
import com.example.playlistmaker.search.data.api.SearchHistory
import com.example.playlistmaker.search.data.dto.TrackDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val TRACK_HISTORY = "track_history"

class SearchHistoryImpl(private val sharedPreferences: SharedPreferences, private val trackMapper: TrackMapper): SearchHistory {


    var searchHistory: MutableList<TrackDto> = read().map {trackMapper.mapToDto(it)}.toMutableList()

    override fun read(): MutableList<Track> {
        val type = object : TypeToken<MutableList<TrackDto>>() {}.type
        val json = sharedPreferences.getString(TRACK_HISTORY, null) ?: return arrayListOf()
        val trackDtoList: MutableList<TrackDto> = Gson().fromJson(json, type)
        return trackDtoList.map { trackMapper.mapToDomain(it) }.toMutableList()
    }

    override fun write(track: Track) {
        Log.i("history_bug", "Запущен метод write, трек $track")
        searchHistory = read().map {trackMapper.mapToDto(it)}.toMutableList()
        var isHere = false
        var sameTrack: TrackDto? = null
        if (searchHistory.isNotEmpty()) {
            for (i in searchHistory) {
                if (track.trackId == i.trackId) {
                    isHere = true
                    sameTrack = i
                }
            }
        }
        if (isHere) {
            searchHistory.remove(sameTrack)
        }
        searchHistory.add(0, trackMapper.mapToDto(track))
        if (searchHistory.size > 10) {
            searchHistory.removeAt(10)
        }
        val json = Gson().toJson(searchHistory)
        sharedPreferences.edit()
            .putString(TRACK_HISTORY, json)
            .apply()
    }

    override fun clear() {
        searchHistory.clear()
        val json = Gson().toJson(searchHistory)
        sharedPreferences.edit()
            .putString(TRACK_HISTORY, json)
            .apply()
    }

}