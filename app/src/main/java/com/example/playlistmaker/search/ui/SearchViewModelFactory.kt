package com.example.playlistmaker.search.ui

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.search.data.local.TRACK_HISTORY

class SearchViewModelFactory(context: Context): ViewModelProvider.Factory {

    val sharedPreferences = context.getSharedPreferences(TRACK_HISTORY, MODE_PRIVATE)

    val interactor by lazy {
        Creator.provideSearchInteractor(sharedPreferences)
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(interactor = interactor) as T
    }
}