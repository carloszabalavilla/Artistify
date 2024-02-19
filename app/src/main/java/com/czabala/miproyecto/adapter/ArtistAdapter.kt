package com.czabala.miproyecto.adapter

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.czabala.miproyecto.R
import com.czabala.miproyecto.databinding.ViewArtistBinding
import com.czabala.miproyecto.model.artist.Artist

class ArtistAdapter(
    val listener: (Artist) -> Unit
) : RecyclerView.Adapter<ArtistAdapter.ViewHolder>() {
    var artists: List<Artist> = emptyList()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ViewArtistBinding.bind(view)

        fun bind(artist: Artist) {
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
                artistNameView.text = artist.name
                Glide.with(artistImageView)
                    .load(artist.images.first().url)
                    .into(artistImageView)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_artist, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = artists.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(artists[position])
        holder.itemView.setOnClickListener {
            listener(artists[position])
        }
    }
}