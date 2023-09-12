package com.example.playlistmaker.di

import android.content.Context
import com.example.playlistmaker.search.data.SearchRepositoryImpl
import com.example.playlistmaker.search.data.TrackMapper
import com.example.playlistmaker.search.data.api.NetworkClient
import com.example.playlistmaker.search.data.api.SearchHistory
import com.example.playlistmaker.search.data.api.SearchRepository
import com.example.playlistmaker.search.data.local.SearchHistoryImpl
import com.example.playlistmaker.search.data.local.TRACK_HISTORY
import com.example.playlistmaker.search.data.network.RetrofitApi
import com.example.playlistmaker.search.data.network.RetrofitClient
import com.example.playlistmaker.search.domain.api.SearchInteractor
import com.example.playlistmaker.search.domain.impl.SearchInteractorImpl
import com.example.playlistmaker.search.ui.SearchViewModel
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
const val BASE_URL = "https://itunes.apple.com"


val searchModule = module {
    factory { Gson() }

    single {
        TrackMapper()
    }

    single<RetrofitApi> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitApi::class.java)
    }

    single {
        androidContext()
            .getSharedPreferences(TRACK_HISTORY, Context.MODE_PRIVATE)
    }

    single<SearchHistory> {
        SearchHistoryImpl(sharedPreferences = get(), trackMapper = get())
    }

    single<NetworkClient> {
        RetrofitClient(api = get(), context = get())
    }

    single<SearchRepository> {
        SearchRepositoryImpl(networkClient = get(), searchHistory =  get())
    }

    single<SearchInteractor> {
        SearchInteractorImpl(repository = get())
    }

    viewModel {
        SearchViewModel(interactor = get())
    }

}