package com.czabala.miproyecto.ui.fragments

import android.content.Context
import android.os.Bundle
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
import com.czabala.miproyecto.databinding.FragmentNewArtistBinding
import com.czabala.miproyecto.model.RemoteConnection
import com.czabala.miproyecto.model.artist.Artist
import com.czabala.miproyecto.model.searchArtist.SearchArtistResponse
import com.czabala.miproyecto.viewmodel.ArtistViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class NewArtistFragment : Fragment(R.layout.fragment_new_artist) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel: ArtistViewModel by activityViewModels()
        var artist: Artist? = null
        lateinit var artistResponse: Response<SearchArtistResponse>

        FragmentNewArtistBinding.bind(view).apply {
            buttonSearch.setOnClickListener {
                if (editTextName.text.toString().isEmpty()) {
                    editTextName.error = "El nombre no puede estar vacío"
                    return@setOnClickListener
                } else {
                    viewModel.viewModelScope.launch {
                        artistResponse = withContext(Dispatchers.IO) {
                            RemoteConnection.spotifyService.searchArtists(editTextName.text.toString())
                        }
                        if (artistResponse.isSuccessful) {
                            artist =
                                artistResponse.body()?.artists?.items?.firstOrNull()?.data?.uri?.let {
                                    RemoteConnection.spotifyService.getArtists(
                                        it.substringAfterLast(":")
                                    )
                                }?.body()?.artists?.first()
                            if (artist != null) {
                                addLayout.visibility = View.VISIBLE
                                Glide.with(imageViewArtist)
                                    .load(artist!!.images.firstOrNull()?.url)
                                    .into(imageViewArtist)
                                textViewArtist.text = artist!!.name
                                Toast.makeText(
                                    requireContext(),
                                    "Artista encontrado",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "No se encontró el artista",
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
                if (addLayout.visibility == View.VISIBLE) {
                    addLayout.visibility = View.GONE
                } else {
                    findNavController().navigateUp()
                }
            }

            buttonAdd.setOnClickListener {
                for (i in 0 until (viewModel.artistList.value?.size ?: 0)) {
                    if (viewModel.artistList.value?.get(i)?.uri.equals(
                            artistResponse.body()?.artists?.items?.firstOrNull()?.data?.uri
                        )
                    ) {
                        Toast.makeText(
                            requireContext(),
                            "El artista ya está en la lista",
                            Toast.LENGTH_LONG
                        ).show()
                        return@setOnClickListener
                    }
                }
                if (artist != null) {
                    val alertDialogBuilder = AlertDialog.Builder(requireContext())
                    alertDialogBuilder.apply {
                        setTitle("Confirmar acción")
                        setMessage("¿Deseas añadir este artista a la lista?")
                        setPositiveButton("Sí") { dialog, which ->
                            viewModel.viewModelScope.launch {
                                viewModel.addArtistToArtistsList(artist!!)
                                viewModel.saveArtistOnFirestore(artist!!)
                                Toast.makeText(
                                    requireContext(),
                                    "Artista añadido",
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