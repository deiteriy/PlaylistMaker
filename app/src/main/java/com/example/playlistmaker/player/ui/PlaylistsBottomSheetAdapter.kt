package com.example.playlistmaker.player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.library.domain.models.Playlist

class PlaylistsBottomSheetAdapter(private val onPlaylistClickListener: PlaylistsBottomSheetAdapter.OnPlaylistClickListener): RecyclerView.Adapter<PlaylistsBottomSheetViewHolder>() {

    interface OnPlaylistClickListener {
        fun onPlaylistClick(item: Playlist)
    }

    var data: ArrayList<Playlist> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsBottomSheetViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.playlist_item_bs, parent, false)
        return PlaylistsBottomSheetViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistsBottomSheetViewHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener {
            onPlaylistClickListener.onPlaylistClick(item = data[position])
        }
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