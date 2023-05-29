package com.example.playlistmaker.presentation.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.entity.Track

class TrackListAdapter(private val onTrackClickListener: OnTrackClickListener): RecyclerView.Adapter<TrackListViewHolder>() {

    interface OnTrackClickListener {
        fun onTrackClick(item: Track)
    }

    var data: ArrayList<Track> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.track_item, parent, false)
        return TrackListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: TrackListViewHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener {
            onTrackClickListener.onTrackClick(item = data[position])
        }
    }
    fun setTracks(newTracks: List<Track>?) {
        data.clear()
        if (!newTracks.isNullOrEmpty()) {
            data.addAll(newTracks)
        }
        notifyDataSetChanged()
    }

}