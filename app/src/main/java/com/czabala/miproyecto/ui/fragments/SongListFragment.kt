package com.czabala.miproyecto.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.czabala.miproyecto.R
import com.czabala.miproyecto.adapter.SongAdapter
import com.czabala.miproyecto.databinding.FragmentSongListBinding
import com.czabala.miproyecto.model.track.Track
import com.czabala.miproyecto.viewmodel.SongViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SongListFragment : Fragment(R.layout.fragment_song_list) {
    private lateinit var binding: FragmentSongListBinding
    private val adapter = SongAdapter { song ->
        navigateTo(song)
    }
    private val viewModel: SongViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        binding = FragmentSongListBinding.bind(view).apply {
            songListRecyclerView.adapter = adapter
            viewModel.songList.observe(viewLifecycleOwner) {
                if (viewModel.songChanged.value == true || adapter.itemCount == 0) {
                    loadSongs(it)
                }
            }
            floatingActionButtonAdd.setOnClickListener {
                findNavController().navigate(R.id.action_songListFragment_to_newSongFragment)
            }
            bottomNavigationView?.setOnNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_artists -> {
                        // Navegar al fragmento de artistas
                        navigateToArtistsFragment()
                        true
                    }

                    R.id.action_songs -> {
                        // Navegar al fragmento de canciones
                        true
                    }

                    else -> false
                }
            }
        }
    }

    private fun navigateToArtistsFragment() {
        findNavController().navigate(R.id.action_songListFragment_to_artistListFragment)
    }

    private fun navigateTo(song: Track) {
        viewModel.setSong(song)
        findNavController().navigate(R.id.action_songListFragment_to_songDetailFragment)
    }

    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("NotifyDataSetChanged")
    private fun loadSongs(it: List<Track>) {
        binding.progressBar.visibility = View.VISIBLE
        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                Thread.sleep(1000)
                adapter.songs = it
            }
            adapter.notifyDataSetChanged()
            viewModel.songChanged.postValue(false)
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