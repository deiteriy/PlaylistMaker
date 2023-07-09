package com.example.playlistmaker.settings.domain.impl

import com.example.playlistmaker.settings.data.api.SettingsRepository
import com.example.playlistmaker.settings.domain.api.SettingsInteractor
import com.example.playlistmaker.settings.domain.model.ThemeSettings

class SettingsInteractorImpl(private val repository: SettingsRepository): SettingsInteractor {
    override fun getThemeSettings(): ThemeSettings {
        return repository.getThemeSettings()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        repository.updateThemeSetting(settings)
    }

}