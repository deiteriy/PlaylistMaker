package com.example.playlistmaker.di

import androidx.room.Room
import com.example.playlistmaker.library.ui.viewmodels.FavoritesViewModel
import com.example.playlistmaker.library.ui.viewmodels.PlaylistsViewModel
import com.example.playlistmaker.library.data.db.AppDatabase
import com.example.playlistmaker.library.data.db.HistoryRepositoryImpl
import com.example.playlistmaker.library.data.db.converters.TrackDbConverter
import com.example.playlistmaker.library.domain.db.HistoryInteractor
import com.example.playlistmaker.library.domain.db.HistoryRepository
import com.example.playlistmaker.library.domain.impl.HistoryInteractorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val libraryModule = module {

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }

    factory { TrackDbConverter() }

    single<HistoryRepository> {
        HistoryRepositoryImpl(get(), get())
    }

    single<HistoryInteractor> {
        HistoryInteractorImpl(get())
    }

    viewModel {
        PlaylistsViewModel()
    }

    viewModel {
        FavoritesViewModel()
    }
}