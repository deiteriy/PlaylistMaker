package com.example.playlistmaker.creator

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import com.example.playlistmaker.player.data.api.PlayerRepository
import com.example.playlistmaker.player.data.impl.PlayerRepositoryImpl
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.search.data.SearchRepositoryImpl
import com.example.playlistmaker.search.data.api.NetworkClient
import com.example.playlistmaker.search.data.api.SearchHistory
import com.example.playlistmaker.search.data.api.SearchRepository
import com.example.playlistmaker.search.data.local.SearchHistoryImpl
import com.example.playlistmaker.search.data.network.RetrofitClient
import com.example.playlistmaker.search.domain.api.SearchInteractor
import com.example.playlistmaker.search.domain.impl.SearchInteractorImpl
import com.example.playlistmaker.settings.data.api.SettingsRepository
import com.example.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.example.playlistmaker.settings.data.impl.SettingsThemeStorageImpl
import com.example.playlistmaker.settings.domain.api.SettingsInteractor
import com.example.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.data.api.ExternalNavigator
import com.example.playlistmaker.sharing.domain.api.SharingInteractor
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl

object Creator {

    private var appContext: Context? = null

    fun init(context: Context) {
        appContext = context.applicationContext
    }
    fun getPlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl(MediaPlayer())
    }

    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository())
    }

    fun getExternalNavigator(context: Context): ExternalNavigator {
        return ExternalNavigatorImpl(context)
    }

    fun provideSharingInteractor(context: Context): SharingInteractor {
        return SharingInteractorImpl(getExternalNavigator(context))
    }

    fun getSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(SettingsThemeStorageImpl(context.getSharedPreferences("local_storage", Context.MODE_PRIVATE)))
    }

    fun provideSettingsInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository(context))
    }

    fun getNetworkClient(): NetworkClient {
        return RetrofitClient
    }

    fun getSearchHistory(sharedPreferences: SharedPreferences): SearchHistory {
        return SearchHistoryImpl(sharedPreferences)
    }

    fun getSearchRepository(sharedPreferences: SharedPreferences): SearchRepository {
        return SearchRepositoryImpl(getNetworkClient(), getSearchHistory(sharedPreferences))
    }

    fun provideSearchInteractor(sharedPreferences: SharedPreferences): SearchInteractor {
        return SearchInteractorImpl(getSearchRepository(sharedPreferences))
    }

}