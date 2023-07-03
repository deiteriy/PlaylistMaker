package com.example.playlistmaker.settings.domain.usecase

import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.settings.domain.api.SettingsRepository

class ShowUserAgreement(private val repository: SettingsRepository) {
    fun execute() {
        repository.showUserAgreement()
    }
}