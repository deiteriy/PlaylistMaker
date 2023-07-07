package com.example.playlistmaker.main.data

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.example.playlistmaker.library.ui.LibraryActivity
import com.example.playlistmaker.main.domain.api.MainActivityRepository
import com.example.playlistmaker.search.ui.SearchActivity
import com.example.playlistmaker.settings.ui.SettingsActivity

class MainActivityRepositoryImpl(private val context: Context): MainActivityRepository {
    override fun openSearchActivity() {
        val searchIntent = Intent(context, SearchActivity::class.java)
        startActivity(context, searchIntent, null)
    }

    override fun openLibraryActivity() {
        val libraryIntent = Intent(context, LibraryActivity::class.java)
        startActivity(context, libraryIntent, null)
    }

    override fun openSettingsActivity() {
        val settingsIntent = Intent(context, SettingsActivity::class.java)
        startActivity(context, settingsIntent, null)
    }

}