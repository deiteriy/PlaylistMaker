package com.example.playlistmaker.settings.data.impl

import com.example.playlistmaker.settings.data.api.SettingsRepository
import com.example.playlistmaker.settings.data.api.SettingsThemeStorage
import com.example.playlistmaker.settings.domain.model.ThemeSettings

class SettingsRepositoryImpl(private val storage: SettingsThemeStorage): SettingsRepository {
    override fun getThemeSettings(): ThemeSettings {
        return storage.getThemeSettings()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        storage.saveThemeSettings(settings)
    }
}