package com.easify.easify.ui.favorite.tracks

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.afollestad.materialdialogs.MaterialDialog
import com.easify.easify.R
import com.easify.easify.databinding.FragmentTopTracksBinding
import com.easify.easify.model.Track
import com.easify.easify.ui.base.BaseFragment
import com.easify.easify.ui.favorite.tracks.adapter.TopTracksAdapter
import com.easify.easify.ui.favorite.tracks.data.TopTracksDataSource
import com.easify.easify.ui.player.PlayerViewEvent
import com.easify.easify.ui.player.PlayerViewModel
import com.easify.easify.util.EventObserver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_top_tracks.*

/**
 * @author: deniz.demirci
 * @date: 9/25/2020
 */

@AndroidEntryPoint
class TopTracksFragment : BaseFragment(R.layout.fragment_top_tracks) {

  private val topTracksViewModel by viewModels<TopTracksViewModel>()

  private val playerViewModel by viewModels<PlayerViewModel>()

  private lateinit var binding: FragmentTopTracksBinding

  private val args: TopTracksFragmentArgs by navArgs()

  private lateinit var topTracksAdapter: TopTracksAdapter

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    DataBindingUtil.bind<FragmentTopTracksBinding>(topTracksRoot)?.apply {
      lifecycleOwner = this@TopTracksFragment.viewLifecycleOwner
      topTracksViewModel = this@TopTracksFragment.topTracksViewModel
      binding = this
    }
    setupObservers()
    setupTopArtistsAdapter()
  }

  private fun setupObservers() {
    topTracksViewModel.event.observe(viewLifecycleOwner, EventObserver { event ->
      when (event) {
        TopTracksViewEvent.GetDevices -> getDevices()
        TopTracksViewEvent.Play -> playTrack()
        is TopTracksViewEvent.TrackClicked -> setClickedTrackUri(event.uri)
        is TopTracksViewEvent.AddIconClicked -> onAddClicked(event.track)
        is TopTracksViewEvent.ShowError -> showError(event.message)
      }
    })

    playerViewModel.event.observe(viewLifecycleOwner, EventObserver{ event ->
      when (event) {
        is PlayerViewEvent.DeviceIdSet -> handleDeviceIdSet(event.deviceId)
        PlayerViewEvent.ShowOpenSpotifyWarning -> showOpenSpotifyWarning()
      }
    })

    buildPagedListLiveData().observe(viewLifecycleOwner, { list ->
      topTracksAdapter.submitList(list)
    })
  }

  private fun setupTopArtistsAdapter() {
    topTracksAdapter = TopTracksAdapter(topTracksViewModel)
    binding.topTracksRecyclerView.adapter = topTracksAdapter
  }

  private fun buildPagedListLiveData(): LiveData<PagedList<Track>> {
    return LivePagedListBuilder(
      object : DataSource.Factory<String, Track>() {
        override fun create(): DataSource<String, Track> {
          return TopTracksDataSource(args.timeRange, topTracksViewModel)
        }
      }, 20).build()
  }

  private fun getDevices() {
    playerViewModel.getDevices()
  }

  private fun playTrack() {
    playerViewModel.play(isTrack = true)
  }

  private fun setClickedTrackUri(uri: String) {
    playerViewModel.setUriToPlay(uri)
  }

  private fun onAddClicked(track: Track) {
    val action = TopTracksFragmentDirections
      .actionTopTracksFragmentToAddTrackToPlaylistFragment2(track)
    findNavController().navigate(action)
  }

  private fun handleDeviceIdSet(deviceId: String?) {
    deviceId?.let {
      playerViewModel.play(isTrack = true)
    } ?: run { showOpenSpotifyWarning() }
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
