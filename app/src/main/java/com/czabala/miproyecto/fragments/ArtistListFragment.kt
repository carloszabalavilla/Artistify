package com.czabala.miproyecto.fragments

import android.app.DownloadManager.COLUMN_ID
import android.content.ContentValues
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
import com.czabala.miproyecto.R
import com.czabala.miproyecto.adapters.ArtistAdapter
import com.czabala.miproyecto.databinding.FragmentArtistListBinding
import com.czabala.miproyecto.model.server.artist.Artist
import com.czabala.miproyecto.viewmodels.ArtistViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArtistListFragment : Fragment(R.layout.fragment_artist_list) {
    private lateinit var binding: FragmentArtistListBinding
    private val adapter = ArtistAdapter { artist ->
        navigateTo(artist)
    }
    private val viewModel: ArtistViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentArtistListBinding.bind(view).apply {
            artistListRecyclerView.adapter = adapter
            viewModel.artistList.observe(viewLifecycleOwner) {
                if (viewModel.artistChanged.value == true || adapter.itemCount == 0) {
                    loadArtists(it)
                }
            }
            floatingActionButtonAdd.setOnClickListener {
                findNavController().navigate(R.id.action_artistListFragment_to_newArtistFragment)
            }
        }
    }

    private fun navigateTo(artist: Artist) {
        viewModel.setArtist(artist)
        findNavController().navigate(R.id.action_artistListFragment_to_artistDetailFragment)
    }

    fun loadArtists(it: List<Artist>) {
        binding.progressBar.visibility = View.VISIBLE
        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                Thread.sleep(1000)
                adapter.artists = it
            }
            adapter.notifyDataSetChanged()
            viewModel.artistChanged.postValue(false)
            binding.progressBar.visibility = View.GONE
            if (it.isEmpty()) {
                binding.addIcon.visibility = View.VISIBLE
                binding.arrowIcon.visibility = View.VISIBLE
                binding.textViewEmpty.visibility = View.VISIBLE
            } else {
                binding.addIcon.visibility = View.GONE
                binding.arrowIcon.visibility = View.GONE
                binding.textViewEmpty.visibility = View.GONE
            }
        }
        /*val dbHelper = DatabaseHelper(requireContext())
        val db = dbHelper.writableDatabase

        val values = ContentValues()
        values.put(dbHelper.getColumnName(), it[0].name)

        val newRowId = db.insert(dbHelper.getTableName(), null, values)

        // Cierra la base de datos cuando hayas terminado
        db.close()*/

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
                alertDialog.setMessage("¿Estás seguro de que quieres eliminar todos los artistas?")

                alertDialog.setPositiveButton("Sí") { _, _ ->
                    viewModel.viewModelScope.launch {
                        withContext(Dispatchers.IO) {
                            viewModel.artistList.value?.let {
                                viewModel.deleteAllArtists()
                                requireActivity().runOnUiThread {
                                    Toast.makeText(
                                        requireContext(),
                                        "Artistas eliminados",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    loadArtists(emptyList())
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