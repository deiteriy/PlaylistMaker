package com.example.playlistmaker.di

import androidx.room.Room
import com.example.playlistmaker.library.data.db.AppDatabase
import com.example.playlistmaker.library.data.db.FavoritesRepositoryImpl
import com.example.playlistmaker.library.data.db.PlaylistRepositoryImpl
import com.example.playlistmaker.library.data.db.converters.PlaylistDbConverter
import com.example.playlistmaker.library.data.db.converters.SavedTrackDbConverter
import com.example.playlistmaker.library.data.db.converters.TrackDbConverter
import com.example.playlistmaker.library.domain.db.FavoritesInteractor
import com.example.playlistmaker.library.domain.db.FavoritesRepository
import com.example.playlistmaker.library.domain.db.PlaylistInteractor
import com.example.playlistmaker.library.domain.db.PlaylistRepository
import com.example.playlistmaker.library.domain.impl.FavoritesInteractorImpl
import com.example.playlistmaker.library.domain.impl.PlaylistInteractorImpl
import com.example.playlistmaker.library.ui.viewmodels.CreatePlaylistViewModel
import com.example.playlistmaker.library.ui.viewmodels.FavoritesViewModel
import com.example.playlistmaker.library.ui.viewmodels.PlaylistsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val libraryModule = module {

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }

    factory { TrackDbConverter() }

    factory { PlaylistDbConverter(get()) }

    factory { SavedTrackDbConverter()}

    single<FavoritesRepository> {
        FavoritesRepositoryImpl(get(), get())
    }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(appDatabase = get(), playlistDbConverter =  get(), savedTrackDbConverter = get())
    }

    single<FavoritesInteractor> {
        FavoritesInteractorImpl(get())
    }

    single<PlaylistInteractor> {
        PlaylistInteractorImpl(get())
    }

    viewModel {
        PlaylistsViewModel(playlistInteractor = get())
    }

    viewModel {
        CreatePlaylistViewModel(playlistInteractor = get())
    }

    viewModel {
        FavoritesViewModel(favoritesInteractor = get())
    }
}