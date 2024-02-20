package com.czabala.miproyecto.adapter

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.czabala.miproyecto.R
import com.czabala.miproyecto.databinding.ViewSongBinding
import com.czabala.miproyecto.model.track.Track

class SongAdapter(
    val listener: (Track) -> Unit
) : RecyclerView.Adapter<SongAdapter.ViewHolder>() {
    var songs: List<Track> = emptyList()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ViewSongBinding.bind(view)

        fun bind(track: Track) {
            with(binding) {
                cardView.setOnLongClickListener {
                    val scaleDownX = ObjectAnimator.ofFloat(cardView, "scaleX", 0.8f)
                    val scaleDownY = ObjectAnimator.ofFloat(cardView, "scaleY", 0.8f)

                    scaleDownX.duration = 200
                    scaleDownY.duration = 200

                    val scaleDown = AnimatorSet()
                    scaleDown.play(scaleDownX).with(scaleDownY)

                    scaleDown.start()
                    true
                }
                songNameView.text = track.name
                Glide.with(songImageView)
                    .load(track.album.images.first().url)
                    .into(songImageView)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_song, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = songs.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(songs[position])
        holder.itemView.setOnClickListener {
            listener(songs[position])
        }
    }
}