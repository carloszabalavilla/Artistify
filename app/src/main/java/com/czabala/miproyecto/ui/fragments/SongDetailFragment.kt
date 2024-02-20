package com.czabala.miproyecto.ui.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.czabala.miproyecto.R
import com.czabala.miproyecto.databinding.FragmentSongDetailBinding
import com.czabala.miproyecto.model.RemoteConnection
import com.czabala.miproyecto.model.track.Track
import com.czabala.miproyecto.viewmodel.SongViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SongDetailFragment : Fragment(R.layout.fragment_song_detail) {
    private val viewModel: SongViewModel by activityViewModels()
    private lateinit var song: Track
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FragmentSongDetailBinding.bind(view).apply {
            viewModel.song.observe(viewLifecycleOwner) {
                song = it
                songName.text = it.name
                Glide.with(songImage)
                    .load(it.album.images.first().url)
                    .into(songImage)
            }
            songLayout.setOnClickListener {
                val url = viewModel.song.value?.external_urls?.spotify ?: ""
                if (url.isNotEmpty()) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Ha ocurrido un error al abrir el enlace",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            viewModel.viewModelScope.launch {

                progressBar.visibility = View.VISIBLE

                viewModel.song.observe(viewLifecycleOwner) { song ->
                    view.post {
                        albumTitle.text = song.album.name
                        Glide.with(albumImage)
                            .load(song.album.images.first().url)
                            .into(albumImage)
                    }
                    GlobalScope.launch {
                        val artist =
                            RemoteConnection.spotifyService.getArtists(song.artists.first().id)
                                .body()?.artists?.first()
                        view.post {
                            artistTitle.text = artist?.name
                            Glide.with(artistImage)
                                .load(artist?.images?.first()?.url)
                                .into(artistImage)
                        }
                    }
                }

                Thread.sleep(1000)
                view.post {
                    progressBar.visibility = View.GONE
                    songLayout.visibility = View.VISIBLE
                }
            }
            albumLayout.setOnClickListener {
                val url = viewModel.song.value?.album?.external_urls?.spotify ?: ""
                if (url.isNotEmpty()) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Ha ocurrido un error al abrir el enlace",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            artistLayout.setOnClickListener {
                val url = viewModel.song.value?.artists?.first()?.external_urls?.spotify ?: ""
                if (url.isNotEmpty()) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Ha ocurrido un error al abrir el enlace",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_detail, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> {
                val alertDialog = AlertDialog.Builder(requireContext())
                alertDialog.setTitle("Confirmar acción")
                alertDialog.setMessage("¿Estás seguro de que quieres eliminar esta cancion?")

                alertDialog.setPositiveButton("Sí") { _, _ ->
                    viewModel.viewModelScope.launch {
                        withContext(Dispatchers.IO) {
                                viewModel.removeSong(song)
                                viewModel.deleteSongOnFirestore(song)
                                requireActivity().runOnUiThread {
                                    Toast.makeText(
                                        requireContext(),
                                        "Cancion eliminada",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    findNavController().popBackStack()
                                }
                            }
                    }
                }
                alertDialog.setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                alertDialog.show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
