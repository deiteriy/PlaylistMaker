package com.example.playlistmaker.creator

import android.media.MediaPlayer
import com.example.playlistmaker.player.data.api.PlayerRepository
import com.example.playlistmaker.player.data.impl.PlayerRepositoryImpl
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.impl.PlayerInteractorImpl

object Creator {

   /* private var appContext: Context? = null*/

/*    fun init(context: Context) {
        appContext = context.applicationContext
    }*/
/*
    fun getPlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl(MediaPlayer())
    }
*/

  /*  fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository())
    }*/

  /*  fun getExternalNavigator(context: Context): ExternalNavigator {
        return ExternalNavigatorImpl(context)
    }*/

 /*   fun provideSharingInteractor(context: Context): SharingInteractor {
        return SharingInteractorImpl(getExternalNavigator(context))
    }

    fun getSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(SettingsThemeStorageImpl(context.getSharedPreferences("local_storage", Context.MODE_PRIVATE)))
    }*/

  /*  fun provideSettingsInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository(context))
    }*/
}