package com.easify.easify.ui.profile.playlists.main

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.afollestad.materialdialogs.MaterialDialog
import com.easify.easify.R
import com.easify.easify.databinding.FragmentPlaylistBinding
import com.easify.easify.model.Playlist
import com.easify.easify.ui.base.BaseFragment
import com.easify.easify.ui.player.PlayerViewEvent
import com.easify.easify.ui.profile.playlists.main.adapter.PlaylistAdapter
import com.easify.easify.ui.profile.playlists.main.data.PlaylistDataSource
import com.easify.easify.util.EventObserver
import com.easify.easify.ui.player.PlayerViewModel
import com.easify.easify.ui.profile.follows.FollowedArtistsViewEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_playlist.*

/**
 * @author: deniz.demirci
 * @date: 9/29/2020
 */

@AndroidEntryPoint
class PlaylistFragment : BaseFragment(R.layout.fragment_playlist) {

  private val playlistViewModel by viewModels<PlaylistViewModel>()

  private val playerViewModel by viewModels<PlayerViewModel>()

  private lateinit var binding: FragmentPlaylistBinding

  private lateinit var playlistAdapter: PlaylistAdapter

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    DataBindingUtil.bind<FragmentPlaylistBinding>(playlists_root)?.apply {
      lifecycleOwner = this@PlaylistFragment.viewLifecycleOwner
      playlistViewModel = this@PlaylistFragment.playlistViewModel
      binding = this
    }
    setupListeners()
    setupObservers()
    setupPlaylistAdapter()
  }

  private fun setupListeners() {
    binding.createPlaylist.setOnClickListener {
      findNavController().navigate(R.id.createPlaylistFragment)
    }
  }

  private fun setupObservers() {
    playlistViewModel.event.observe(viewLifecycleOwner, EventObserver { event ->
      when (event) {
        PlaylistViewEvent.GetDevices -> getDevices()
        PlaylistViewEvent.Play -> playPlaylist()
        is PlaylistViewEvent.ListenIconClicked -> setClickedPlaylistUri(event.uri)
        is PlaylistViewEvent.OpenPlaylistDetail -> openPlaylistDetailFragment(event.playlist)
        is PlaylistViewEvent.ShowError -> showError(event.message)
      }
    })

    playerViewModel.event.observe(viewLifecycleOwner, EventObserver{ event ->
      when (event) {
        is PlayerViewEvent.DeviceIdSet -> handleDeviceIdSet(event.deviceId)
        PlayerViewEvent.ShowOpenSpotifyWarning -> showOpenSpotifyWarning()
      }
    })

    buildPagedListLiveData().observe(viewLifecycleOwner, { list ->
      playlistAdapter.submitList(list)
    })
  }

  private fun handleDeviceIdSet(deviceId: String?) {
    deviceId?.let {
      playerViewModel.play()
    } ?: run { showOpenSpotifyWarning() }
  }

  private fun setupPlaylistAdapter() {
    playlistAdapter = PlaylistAdapter(playlistViewModel)
    binding.playlistsRecyclerView.adapter = playlistAdapter
  }

  private fun buildPagedListLiveData(): LiveData<PagedList<Playlist>> {
    return LivePagedListBuilder(
      object : DataSource.Factory<String, Playlist>() {
        override fun create(): DataSource<String, Playlist> {
          return PlaylistDataSource(playlistViewModel)
        }
      }, 20).build()
  }

  private fun getDevices() {
    playerViewModel.getDevices()
  }

  private fun setClickedPlaylistUri(uri: String) {
    playerViewModel.setUriToPlay(uri)
  }

  private fun playPlaylist() {
    playerViewModel.play()
  }

  private fun openPlaylistDetailFragment(playlist: Playlist) {
    val action = PlaylistFragmentDirections.actionPlaylistFragmentToPlaylistDetailFragment(playlist)
    findNavController().navigate(action)
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
