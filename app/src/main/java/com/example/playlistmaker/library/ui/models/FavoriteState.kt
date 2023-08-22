package com.example.playlistmaker.library.ui.models

import com.example.playlistmaker.player.domain.models.Track

sealed class FavoriteState {
    class FavoriteFullState(val tracks: List<Track>): FavoriteState()
    class FavoriteEmptyState: FavoriteState()
}