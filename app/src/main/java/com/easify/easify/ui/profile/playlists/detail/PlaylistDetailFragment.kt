package com.easify.easify.ui.profile.playlists.detail

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.afollestad.materialdialogs.MaterialDialog
import com.easify.easify.R
import com.easify.easify.databinding.FragmentPlaylistDetailBinding
import com.easify.easify.model.util.EasifyItem
import com.easify.easify.ui.base.BaseFragment
import com.easify.easify.ui.common.adapter.EasifyItemListAdapter
import com.easify.easify.ui.extensions.dpToPx
import com.easify.easify.ui.player.PlayerViewEvent
import com.easify.easify.ui.player.PlayerViewModel
import com.easify.easify.util.EventObserver
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_playlist_detail.*

/**
 * @author: deniz.demirci
 * @date: 10/3/2020
 */

private const val BOTTOM_NAV_VIEW_HEIGHT = 48

@AndroidEntryPoint
class PlaylistDetailFragment : BaseFragment(R.layout.fragment_playlist_detail) {

  private val playlistDetailViewModel by viewModels<PlaylistDetailViewModel>()

  private val playerViewModel by viewModels<PlayerViewModel>()

  private val args: PlaylistDetailFragmentArgs by navArgs()

  private lateinit var binding: FragmentPlaylistDetailBinding

  private lateinit var easifyItemListAdapter: EasifyItemListAdapter

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    DataBindingUtil.bind<FragmentPlaylistDetailBinding>(playlist_detail_root)?.apply {
      lifecycleOwner = this@PlaylistDetailFragment.viewLifecycleOwner
      playlistDetailViewModel = this@PlaylistDetailFragment.playlistDetailViewModel
      binding = this
    }
    args.playlist?.let { playlist ->
      playlistDetailViewModel.initialize(playlist)
    } ?: run { findNavController().popBackStack() }
    // TODO: inform user
    setupObservers()
    setupPlaylistDetailAdapter()
  }

  private fun setupObservers() {
    playlistDetailViewModel.event.observe(viewLifecycleOwner, EventObserver { event ->
      when (event) {
        PlaylistDetailViewEvent.GetDevices -> getDevices()
        PlaylistDetailViewEvent.Play -> playPlaylist()
        is PlaylistDetailViewEvent.ListenIconClicked -> setClickedTrackUri(event.uri)
        is PlaylistDetailViewEvent.ShowError -> showError(event.message)
        is PlaylistDetailViewEvent.NotifyDataChanged -> {
          easifyItemListAdapter.submitList(event.tracks)
        }
        is PlaylistDetailViewEvent.ShowSnackbar -> {
          showSnackbar(getString(R.string.fragment_playlist_detail_removed, event.trackName))
        }
      }
    })

    playerViewModel.event.observe(viewLifecycleOwner, EventObserver{ event ->
      when (event) {
        is PlayerViewEvent.DeviceIdSet -> handleDeviceIdSet(event.deviceId)
        PlayerViewEvent.ShowOpenSpotifyWarning -> showOpenSpotifyWarning()
      }
    })
  }

  private fun setupPlaylistDetailAdapter() {
    easifyItemListAdapter = EasifyItemListAdapter(playlistDetailViewModel, ::removeClicked)
    binding.tracksRecyclerView.adapter = easifyItemListAdapter
  }

  private fun removeClicked(item: EasifyItem) {
    item.track?.let { track ->
      playlistDetailViewModel.removeTrack(track)
    }
  }

  private fun handleDeviceIdSet(deviceId: String?) {
    deviceId?.let {
      playerViewModel.play()
    } ?: run { showOpenSpotifyWarning() }
  }

  private fun getDevices() {
    playerViewModel.getDevices()
  }

  private fun setClickedTrackUri(uri: String) {
    playerViewModel.setUriToPlay(uri)
  }

  private fun playPlaylist() {
    playerViewModel.play(playlistUri = args.playlist?.uri)
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

  private fun showSnackbar(message: String) {
    view?.let {
      val snackbar = Snackbar.make(it, message, Snackbar.LENGTH_LONG)
      context?.let { safeContext ->
        snackbar.view.background = ContextCompat.getDrawable(safeContext, R.drawable.bg_snackbar)
      }
      val params = snackbar.view.layoutParams as ViewGroup.MarginLayoutParams
      params.setMargins(24, 24, 24, 48 + BOTTOM_NAV_VIEW_HEIGHT.dpToPx())
      snackbar.view.layoutParams = params
      snackbar.show()
    }
  }
}
