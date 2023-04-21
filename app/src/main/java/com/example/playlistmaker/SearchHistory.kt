package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val TRACK_HISTORY = "track_history"

class SearchHistory(sharedPreferences: SharedPreferences) {
    var searchHistory: MutableList<Track> = read(sharedPreferences)

    fun read(sharedPreferences: SharedPreferences): MutableList<Track> {
        val type = object : TypeToken<MutableList<Track>>() {}.type
        val json = sharedPreferences.getString(TRACK_HISTORY, null) ?: return arrayListOf()
        return Gson().fromJson(json, type)
    }

    fun write(sharedPreferences: SharedPreferences, track: Track) {
        searchHistory = read(sharedPreferences)
        var isHere = false
        var sameTrack: Track? = null
        if(searchHistory.isNotEmpty()) {
            for(i in searchHistory) {
                if(track.trackId == i.trackId) {
                    isHere = true
                    sameTrack = i
                }
            }
        }
        if(isHere) { searchHistory.remove(sameTrack) }
        searchHistory.add(0, track)
        if(searchHistory.size > 10) {
            searchHistory.removeAt(10)
        }
        val json = Gson().toJson(searchHistory)
        sharedPreferences.edit()
            .putString(TRACK_HISTORY, json)
            .apply()
    }

    fun clearHistory(sharedPreferences: SharedPreferences) {
        searchHistory.clear()
        val json = Gson().toJson(searchHistory)
        sharedPreferences.edit()
            .putString(TRACK_HISTORY, json)
            .apply()
    }

}