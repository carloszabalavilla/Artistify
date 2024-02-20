package com.czabala.miproyecto.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.czabala.miproyecto.R
import com.czabala.miproyecto.databinding.FragmentNewSongBinding
import com.czabala.miproyecto.model.RemoteConnection
import com.czabala.miproyecto.model.searchTrack.SearchTrackResponse
import com.czabala.miproyecto.model.track.Track
import com.czabala.miproyecto.viewmodel.SongViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class NewSongFragment : Fragment(R.layout.fragment_new_song) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel: SongViewModel by activityViewModels()
        var song: Track? = null
        lateinit var songResponse: Response<SearchTrackResponse>

        FragmentNewSongBinding.bind(view).apply {
            buttonSearch.setOnClickListener {
                if (editTextName.text.toString().isEmpty()) {
                    editTextName.error = "El nombre no puede estar vacío"
                    return@setOnClickListener
                } else {
                    viewModel.viewModelScope.launch {
                        songResponse = withContext(Dispatchers.IO) {
                            RemoteConnection.spotifyService.searchTracks(editTextName.text.toString())
                        }
                        if (songResponse.isSuccessful) {
                            song =
                                songResponse.body()?.tracks?.items?.first()?.data?.uri?.let {
                                    RemoteConnection.spotifyService.getTracks(
                                        it.substringAfterLast(":")
                                    )
                                }?.body()?.tracks?.first()
                            if (song != null) {
                                addLayout.visibility = View.VISIBLE
                                Glide.with(imageViewSong)
                                    .load(song!!.album.images.first().url)
                                    .into(imageViewSong)
                                textViewSong.text = song!!.name
                                Toast.makeText(
                                    requireContext(),
                                    "Cancion encontrada",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "No se encontró la canción",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                    val context = requireContext()

                    val imm =
                        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)

                }
            }
            buttonCancel.setOnClickListener {
                findNavController().navigateUp()
            }
            buttonAdd.setOnClickListener {
                for (i in 0 until (viewModel.songList.value?.size ?: 0)) {
                    if (viewModel.songList.value?.get(i)?.uri.equals(
                            songResponse.body()?.tracks?.items?.first()?.data?.uri
                        )
                    ) {
                        val toast = Toast.makeText(
                            requireContext(),
                            "La cancion ya está en la lista",
                            Toast.LENGTH_LONG
                        )
                        toast.setGravity(Gravity.TOP or Gravity.CENTER, 0, 50)
                        toast.show()
                        return@setOnClickListener
                    }
                }
                if (song != null) {
                    val alertDialogBuilder = AlertDialog.Builder(requireContext())
                    alertDialogBuilder.apply {
                        setTitle("Confirmar acción")
                        setMessage("¿Deseas añadir esta cancion a la lista?")
                        setPositiveButton("Sí") { dialog, which ->
                            viewModel.viewModelScope.launch {
                                viewModel.addSongToSongsList(song!!)
                                viewModel.saveSongOnFirestore(song!!)
                                Toast.makeText(
                                    requireContext(),
                                    "Cancion añadida",
                                    Toast.LENGTH_LONG
                                ).show()
                                findNavController().popBackStack()
                            }
                        }
                        setNegativeButton("No") { dialog, _ ->
                            dialog.dismiss()
                        }
                    }
                    alertDialogBuilder.create().show()
                }
            }
        }
    }
}