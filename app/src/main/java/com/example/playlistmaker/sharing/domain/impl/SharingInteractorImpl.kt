package com.example.playlistmaker.sharing.domain.impl

import com.example.playlistmaker.sharing.domain.api.ExternalNavigator
import com.example.playlistmaker.sharing.domain.api.SharingInteractor
import com.example.playlistmaker.sharing.domain.model.EmailData

class SharingInteractorImpl(private val externalNavigator: ExternalNavigator): SharingInteractor {
    override fun showUserAgreement() {
        externalNavigator.showUserAgreement(getTermsLink())
    }

    override fun shareApp() {
        externalNavigator.shareApp(getShareAppLink())
    }

    override fun sendMailToSupport() {
        externalNavigator.sendMailToSupport(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return APP_LINK
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(mail = SUPPORT_EMAIL)
    }

    private fun getTermsLink(): String {
        return TERM_LINK
    }

    companion object {
        const val APP_LINK = "https://practicum.yandex.ru/android-developer"
        const val SUPPORT_EMAIL = "musicdeity@yandex.ru"
        const val TERM_LINK = "https://yandex.ru/legal/practicum_offer"
    }
}