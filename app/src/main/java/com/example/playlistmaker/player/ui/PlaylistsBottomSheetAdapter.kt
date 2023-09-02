package com.example.playlistmaker.player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.library.ui.PlaylistsViewHolder

class PlaylistsBottomSheetAdapter: RecyclerView.Adapter<PlaylistsBottomSheetViewHolder>() {

    var data: ArrayList<Playlist> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsBottomSheetViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.playlist_item_bs, parent, false)
        return PlaylistsBottomSheetViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistsBottomSheetViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setPlaylists(newPlaylists: List<Playlist>?) {
        data.clear()
        if (!newPlaylists.isNullOrEmpty()) {
            data.addAll(newPlaylists)
        }
        notifyDataSetChanged()
    }
}