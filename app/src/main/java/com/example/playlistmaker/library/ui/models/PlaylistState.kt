package com.example.playlistmaker.library.ui.models

import com.example.playlistmaker.library.domain.models.Playlist

sealed class PlaylistState {
    class PlaylistFullState(val playlists: List<Playlist>): PlaylistState()
    class PlaylistEmptyState: PlaylistState()
}