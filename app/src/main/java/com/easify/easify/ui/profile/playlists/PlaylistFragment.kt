package com.easify.easify.ui.profile.playlists

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.afollestad.materialdialogs.MaterialDialog
import com.easify.easify.R
import com.easify.easify.databinding.FragmentPlaylistBinding
import com.easify.easify.model.Playlist
import com.easify.easify.ui.base.BaseFragment
import com.easify.easify.ui.profile.playlists.adapter.PlaylistAdapter
import com.easify.easify.ui.profile.playlists.data.PlaylistDataSource
import com.easify.easify.util.EventObserver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_playlist.*

/**
 * @author: deniz.demirci
 * @date: 9/29/2020
 */

@AndroidEntryPoint
class PlaylistFragment : BaseFragment(R.layout.fragment_playlist) {

  private val viewModel by viewModels<PlaylistViewModel>()

  private lateinit var binding: FragmentPlaylistBinding

  private lateinit var playlistAdapter: PlaylistAdapter

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    DataBindingUtil.bind<FragmentPlaylistBinding>(playlists_root)?.apply {
      lifecycleOwner = this@PlaylistFragment.viewLifecycleOwner
      viewModel = this@PlaylistFragment.viewModel
      binding = this
    }
    setupObservers()
    setupPlaylistAdapter()
  }

  private fun setupObservers() {
    viewModel.event.observe(viewLifecycleOwner, EventObserver { event ->
      when (event) {
        PlaylistViewEvent.ShowOpenSpotifyWarning -> showOpenSpotifyWarning()
        is PlaylistViewEvent.ShowError -> showError(event.message)
      }
    })

    buildPagedListLiveData().observe(viewLifecycleOwner, { list ->
      playlistAdapter.submitList(list)
    })
  }

  private fun setupPlaylistAdapter() {
    playlistAdapter = PlaylistAdapter(viewModel)
    binding.playlistsRecyclerView.adapter = playlistAdapter
  }

  private fun buildPagedListLiveData(): LiveData<PagedList<Playlist>> {
    return LivePagedListBuilder(
      object : DataSource.Factory<String, Playlist>() {
        override fun create(): DataSource<String, Playlist> {
          return PlaylistDataSource(viewModel)
        }
      }, 20).build()
  }

  private fun showOpenSpotifyWarning() {
    MaterialDialog(requireContext()).show {
      title(R.string.dialog_error_title)
      message(R.string.dialog_should_open_spotify)
      positiveButton(R.string.dialog_ok)
    }
  }

  private fun showError(message: String) {
    MaterialDialog(requireContext()).show {
      title(R.string.dialog_error_title)
      message(text = message)
      positiveButton(R.string.dialog_ok)
    }
  }
}
