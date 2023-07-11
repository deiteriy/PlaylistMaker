package com.example.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import com.example.playlistmaker.search.data.api.NetworkClient
import com.example.playlistmaker.search.data.api.SearchHistory
import com.example.playlistmaker.search.data.local.SearchHistoryImpl
import com.example.playlistmaker.search.data.network.RetrofitApi
import com.example.playlistmaker.search.data.network.RetrofitClient
import com.example.playlistmaker.settings.data.api.SettingsThemeStorage
import com.example.playlistmaker.settings.data.impl.SettingsThemeStorageImpl
import com.example.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.data.api.ExternalNavigator
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val searchDataModule = module {

    single<RetrofitApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitApi::class.java)
    }

    single {
        androidContext()
            .getSharedPreferences("track_history", Context.MODE_PRIVATE)
    }

    factory { Gson() }

    single<SearchHistory> {
        SearchHistoryImpl(sharedPreferences = get())
    }

    single<NetworkClient> {
        RetrofitClient(api = get())
    }

    single<MediaPlayer> {
        get()
    }

    single<SettingsThemeStorage> {
        SettingsThemeStorageImpl(sharedPreferences = get())
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(context = get())
    }



}
