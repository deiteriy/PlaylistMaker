package com.example.playlistmaker.library.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.library.domain.models.Playlist


class PlaylistsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val playlistCover: ImageView = itemView.findViewById(R.id.playlist_cover)
    private val playlistTitle: TextView = itemView.findViewById(R.id.playlist_title)
    private val tracksCounter: TextView = itemView.findViewById(R.id.tracks_counter)

    fun bind(item: Playlist) {
        playlistTitle.text = item.name
        tracksCounter.text = setTracksCount(item.tracksCount.toInt())
        Glide.with(playlistCover)
            .load(item.playlistCover)
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.playlist_cover_corner_radius)))
            .placeholder(R.drawable.albumcover_placeholder)
            .into(playlistCover)
    }

    private fun setTracksCount(count: Int): String {
        val tracksWord: String = when {
            count % 100 in 11..19 -> "треков"
            count % 10 == 1 -> "трек"
            count % 10 in 2..4 -> "трека"
            else -> "треков"
        }
        return "$count $tracksWord"
    }
}