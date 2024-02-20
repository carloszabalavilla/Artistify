package com.czabala.miproyecto.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.czabala.miproyecto.R
import com.czabala.miproyecto.adapter.ArtistAdapter
import com.czabala.miproyecto.databinding.FragmentArtistListBinding
import com.czabala.miproyecto.model.artist.Artist
import com.czabala.miproyecto.viewmodel.ArtistViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

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

    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("NotifyDataSetChanged")
    private fun loadArtists(it: List<Artist>) {
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
    }
}