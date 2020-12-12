package com.easify.easify.ui.home

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.easify.easify.R
import com.easify.easify.databinding.FragmentHomeBinding
import com.easify.easify.model.SearchType
import com.easify.easify.model.util.EasifyItem
import com.easify.easify.model.util.EasifyTrack
import com.easify.easify.ui.base.BaseFragment
import com.easify.easify.ui.common.adapter.EasifyItemListAdapter
import com.easify.easify.ui.player.PlayerViewEvent
import com.easify.easify.ui.player.PlayerViewModel
import com.easify.easify.ui.search.SearchViewEvent
import com.easify.easify.ui.search.SearchViewModel
import com.easify.easify.util.EventObserver
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author: deniz.demirci
 * @date: 9/4/2020
 */

@AndroidEntryPoint
class HomeFragment : BaseFragment(R.layout.fragment_home) {

  private val searchViewModel by viewModels<SearchViewModel>()

  private val playerViewModel by viewModels<PlayerViewModel>()

  private lateinit var binding: FragmentHomeBinding

  private lateinit var easifyItemAdapter: EasifyItemListAdapter

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val root = view.findViewById<ConstraintLayout>(R.id.home_fragment_root)
    DataBindingUtil.bind<FragmentHomeBinding>(root)?.apply {
      binding = this
    }
    showBottomNavigation(true)
    setAdapters()
    setupObservers()
    setListeners()
  }

  private fun setListeners() {
    binding.search.onSearch = { input ->
      searchViewModel.search(SearchType.TRACK, input)
    }
  }

  private fun setAdapters() {
    easifyItemAdapter = EasifyItemListAdapter(searchViewModel)
    binding.searchTracks.adapter = easifyItemAdapter
  }

  private fun setupObservers() {
    searchViewModel.event.observe(viewLifecycleOwner, EventObserver { event ->
      when (event) {
        SearchViewEvent.GetDevices -> getDevices()
        SearchViewEvent.Play -> playTrack()
        is SearchViewEvent.OnTrackClicked -> onTrackClicked(event.track)
        is SearchViewEvent.OnListenIconClicked -> setClickedTrackUri(event.uri)
        is SearchViewEvent.OnAddIconClicked -> onAddIconClicked(event.track)
        is SearchViewEvent.NotifyTrackDataChanged -> onViewDataChange(event.trackList)
        is SearchViewEvent.ShowError -> showError(event.message)
        else -> Unit
      }
    })

    playerViewModel.event.observe(viewLifecycleOwner, EventObserver{ event ->
      when (event) {
        is PlayerViewEvent.DeviceIdSet -> handleDeviceIdSet(event.deviceId)
        PlayerViewEvent.ShowOpenSpotifyWarning -> showOpenSpotifyWarning()
      }
    })
  }

  private fun handleDeviceIdSet(deviceId: String?) {
    deviceId?.let {
      playTrack()
    } ?: run { showOpenSpotifyWarning() }
  }

  private fun playTrack() {
    playerViewModel.play(isTrack = true)
  }

  private fun getDevices() {
    playerViewModel.getDevices()
  }

  private fun onTrackClicked(track: EasifyTrack) {
    val action = HomeFragmentDirections.actionHomeFragmentToFeaturesFragment(track)
    findNavController().navigate(action)
  }

  private fun onAddIconClicked(track: EasifyTrack) {
    val action = HomeFragmentDirections.actionHomeFragmentToAddTrackToPlaylistFragment3(track)
    findNavController().navigate(action)
  }

  private fun onViewDataChange(items: ArrayList<EasifyItem>) {
    easifyItemAdapter.submitList(items)
  }

  private fun setClickedTrackUri(uri: String) {
    playerViewModel.setUriToPlay(uri)
  }

  private fun showError(message: String) {
    MaterialDialog(requireContext()).show {
      title(R.string.dialog_error_title)
      message(text = message)
      positiveButton(R.string.dialog_ok)
    }
  }

  private fun showOpenSpotifyWarning() {
    MaterialDialog(requireContext()).show {
      title(R.string.dialog_error_title)
      message(R.string.dialog_should_open_spotify)
      positiveButton(R.string.dialog_ok)
    }
  }
}
