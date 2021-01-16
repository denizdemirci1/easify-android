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
import com.easify.easify.R
import com.easify.easify.databinding.FragmentPlaylistBinding
import com.easify.easify.model.util.EasifyItem
import com.easify.easify.model.util.EasifyPlaylist
import com.easify.easify.ui.base.BaseFragment
import com.easify.easify.ui.common.adapter.EasifyItemPagedListAdapter
import com.easify.easify.ui.player.PlayerViewEvent
import com.easify.easify.ui.profile.playlists.main.data.PlaylistDataSource
import com.easify.easify.util.EventObserver
import com.easify.easify.ui.player.PlayerViewModel
import com.spotify.sdk.android.authentication.AuthenticationResponse
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

  private lateinit var easifyItemPagedListAdapter: EasifyItemPagedListAdapter

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    DataBindingUtil.bind<FragmentPlaylistBinding>(playlists_root)?.apply {
      lifecycleOwner = this@PlaylistFragment.viewLifecycleOwner
      playlistViewModel = this@PlaylistFragment.playlistViewModel
      binding = this
    }
    setupListeners()
    setupObservers()
    setupPaging()
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
        PlaylistViewEvent.Authenticate -> openSpotifyLoginActivity(::afterLogin)
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
        PlayerViewEvent.Authenticate -> openSpotifyLoginActivity(::afterLogin)
      }
    })
  }

  private fun setupPaging() {
    buildPagedListLiveData().observe(viewLifecycleOwner, { list ->
      easifyItemPagedListAdapter.submitList(list)
    })
  }

  private fun afterLogin(
    responseType: AuthenticationResponse.Type?,
    response: String
  ) {
    when (responseType) {
      AuthenticationResponse.Type.TOKEN -> {
        playlistViewModel.saveToken(response)
        setupPaging()
      }
      AuthenticationResponse.Type.ERROR -> playlistViewModel.clearToken()
      else -> Unit
    }
  }

  private fun handleDeviceIdSet(deviceId: String?) {
    deviceId?.let {
      playerViewModel.play()
    } ?: run { showOpenSpotifyWarning() }
  }

  private fun setupPlaylistAdapter() {
    easifyItemPagedListAdapter = EasifyItemPagedListAdapter(playlistViewModel)
    binding.playlistsRecyclerView.adapter = easifyItemPagedListAdapter
  }

  private fun buildPagedListLiveData(): LiveData<PagedList<EasifyItem>> {
    return LivePagedListBuilder(
      object : DataSource.Factory<String, EasifyItem>() {
        override fun create(): DataSource<String, EasifyItem> {
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

  private fun openPlaylistDetailFragment(playlist: EasifyPlaylist) {
    val action = PlaylistFragmentDirections.actionPlaylistFragmentToPlaylistDetailFragment(playlist)
    findNavController().navigate(action)
  }
}
