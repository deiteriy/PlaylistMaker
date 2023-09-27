package com.example.playlistmaker.library.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.library.domain.models.Playlist

class PlaylistsAdapter(private val onPlaylistClickListener: OnPlaylistClickListener): RecyclerView.Adapter<PlaylistsViewHolder>()  {

    interface OnPlaylistClickListener {
        fun onPlaylistClick(item: Playlist)
    }


    var data: ArrayList<Playlist> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.playlist_item, parent, false)
        return PlaylistsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: PlaylistsViewHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener {
            onPlaylistClickListener.onPlaylistClick(item = data[position])
        }
    }

    fun setPlaylists(newPlaylists: List<Playlist>?) {
        data.clear()
        if (!newPlaylists.isNullOrEmpty()) {
            data.addAll(newPlaylists)
        }
        notifyDataSetChanged()
    }
}