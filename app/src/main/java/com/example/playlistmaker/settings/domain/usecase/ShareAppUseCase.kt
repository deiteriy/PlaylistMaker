package com.example.playlistmaker.settings.domain.usecase

import android.content.Intent
import com.example.playlistmaker.R
import com.example.playlistmaker.settings.domain.api.SettingsRepository

class ShareAppUseCase(private val repository: SettingsRepository) {
    fun execute() {
        repository.shareApp()
    }
}