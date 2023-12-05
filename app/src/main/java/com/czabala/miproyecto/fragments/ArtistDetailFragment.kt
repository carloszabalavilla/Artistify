package com.czabala.miproyecto.fragments

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
import com.czabala.miproyecto.databinding.FragmentArtistDetailBinding
import com.czabala.miproyecto.model.server.RemoteConnection
import com.czabala.miproyecto.viewmodels.ArtistViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArtistDetailFragment : Fragment(R.layout.fragment_artist_detail) {
    private val viewModel: ArtistViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FragmentArtistDetailBinding.bind(view).apply {
            viewModel.artist.observe(viewLifecycleOwner) {
                artistName.text = it.name
                Glide.with(artistImage)
                    .load(it.images.first().url)
                    .into(artistImage)
            }
            artistLayout.setOnClickListener {
                val url = viewModel.artist.value?.external_urls?.spotify ?: ""
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
                try {
                    val topTracksResponse = withContext(Dispatchers.IO) {
                        RemoteConnection.spotifyService.getTopTracks(
                            viewModel.artist.value?.id ?: ""
                        )
                    }
                    if (topTracksResponse.isSuccessful) {
                        val topTracks = topTracksResponse.body()?.data?.artist?.discography?.singles

                        if (topTracks?.items?.isNotEmpty() == true) {
                            songOne.text = topTracks.items[0].releases.items[0].name
                            Glide.with(songOneImage)
                                .load(topTracks.items[0].releases.items[0].coverArt.sources[0].url)
                                .into(songOneImage)
                            songTwo.text = topTracks.items[1].releases.items[0].name
                            Glide.with(songTwoImage)
                                .load(topTracks.items[1].releases.items[0].coverArt.sources[0].url)
                                .into(songTwoImage)
                            songThree.text = topTracks.items[2].releases.items[0].name
                            Glide.with(songThreeImage)
                                .load(topTracks.items[2].releases.items[0].coverArt.sources[0].url)
                                .into(songThreeImage)

                            songOneLayout.setOnClickListener {
                                val url =
                                    topTracks.items.getOrNull(0)?.releases?.items?.getOrNull(0)?.sharingInfo?.shareUrl
                                        ?: ""
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

                            songTwoLayout.setOnClickListener {
                                val url =
                                    topTracks.items.getOrNull(1)?.releases?.items?.getOrNull(0)?.sharingInfo?.shareUrl
                                        ?: ""
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

                            songThreeLayout.setOnClickListener {
                                val url =
                                    topTracks.items.getOrNull(2)?.releases?.items?.getOrNull(0)?.sharingInfo?.shareUrl
                                        ?: ""
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
                        } else {
                            songOne.text = "No hay canciones"
                            songTwoLayout.visibility = View.GONE
                            songThreeLayout.visibility = View.GONE
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "No se pudieron obtener las canciones principales del artista",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Ha ocurrido un error", Toast.LENGTH_LONG)
                        .show()
                    Toast.makeText(
                        requireContext(),
                        "Ha ocurrido un error",
                        Toast.LENGTH_LONG
                    ).show()
                    println("Excepción lanzada: ${e.message}")
                }
                Thread.sleep(1000)
                progressBar.visibility = View.GONE
                songsLayout.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_artist_detail, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> {
                val alertDialog = AlertDialog.Builder(requireContext())
                alertDialog.setTitle("Confirmar acción")
                alertDialog.setMessage("¿Estás seguro de que quieres eliminar este artista?")

                alertDialog.setPositiveButton("Sí") { _, _ ->
                    viewModel.viewModelScope.launch {
                        withContext(Dispatchers.IO) {
                            viewModel.artistList.value?.let {
                                viewModel.artist.value?.let { artist ->
                                    viewModel.deleteArtist(artist)
                                    requireActivity().runOnUiThread {
                                        Toast.makeText(
                                            requireContext(),
                                            "Artista eliminado",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        findNavController().popBackStack()
                                    }
                                }
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