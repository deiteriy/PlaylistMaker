package com.example.playlistmaker.main.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.main.data.MainActivityRepositoryImpl
import com.example.playlistmaker.main.domain.impl.MainActivityInteractorImpl

class MainViewModelFactory(context: Context): ViewModelProvider.Factory {

    private val interactor by lazy {
        MainActivityInteractorImpl(MainActivityRepositoryImpl(context))
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(
            interactor = interactor
        ) as T
    }

}