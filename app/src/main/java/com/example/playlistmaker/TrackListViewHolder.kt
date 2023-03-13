package com.example.playlistmaker

import android.view.RoundedCorner
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val rootLayout: LinearLayout = itemView.findViewById(R.id.trackRootLayout)
    private val trackCover: ImageView = itemView.findViewById(R.id.trackCover)
    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val artistName: TextView = itemView.findViewById(R.id.artistName)
    private val trackTime: TextView = itemView.findViewById(R.id.trackTime)

    fun bind(item: Track) {
        trackName.text = item.trackName
        artistName.text = item.artistName
        trackTime.text = item.trackTime
        Glide.with(trackCover)
            .load(item.artworkUrl100)
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.cover_corner_radius)))
            .placeholder(R.drawable.albumcover_placeholder)
            .into(trackCover)
    }

}