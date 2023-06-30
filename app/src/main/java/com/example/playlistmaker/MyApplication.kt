package com.example.playlistmaker

import android.app.Application
import android.media.MediaPlayer
import com.example.playlistmaker.data.PlayerRepositoryImpl
import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.impl.PlayerInteractorImpl

class MyApplication {
    fun getRepository(): PlayerRepositoryImpl {
        return PlayerRepositoryImpl(MediaPlayer())
    }

    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(getRepository())
    }
}