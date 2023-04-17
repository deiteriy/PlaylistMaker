package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson

const val TRACK_HISTORY = "track_history"

class SearchHistory(sharedPreferences: SharedPreferences) {
    var searchHistory: MutableList<Track> = read(sharedPreferences)

    fun read(sharedPreferences: SharedPreferences): MutableList<Track> {
        val json = sharedPreferences.getString(TRACK_HISTORY, null) ?: return mutableListOf()
        return Gson().fromJson(json, MutableList::class.java) as MutableList<Track>
    }

    fun write(sharedPreferences: SharedPreferences, track: Track) {
        searchHistory = read(sharedPreferences)
        if(searchHistory.isNotEmpty()) {
            for(i in searchHistory) {
                if(track.trackId == i.trackId) {
                    searchHistory.remove(i)
                }
            }
        }
        searchHistory.add(track)
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