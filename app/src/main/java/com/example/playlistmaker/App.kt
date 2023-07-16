package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.di.libraryModule
import com.example.playlistmaker.di.playerModule
import com.example.playlistmaker.di.searchModule
import com.example.playlistmaker.di.settingsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    private var isDarkTheme = false

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(searchModule, playerModule, settingsModule, libraryModule)
        }

    }
        fun switchTheme(darkThemeEnabled: Boolean) {
        isDarkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}