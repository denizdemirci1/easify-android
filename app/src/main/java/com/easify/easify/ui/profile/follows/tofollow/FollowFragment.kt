package com.easify.easify.ui.profile.follows.tofollow

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.easify.easify.R
import com.easify.easify.databinding.FragmentFollowBinding
import com.easify.easify.model.SearchType
import com.easify.easify.model.util.EasifyArtist
import com.easify.easify.model.util.EasifyItem
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
 * @date: 28.11.2020
 */

@AndroidEntryPoint
class FollowFragment : BaseFragment(R.layout.fragment_follow) {

  private val searchViewModel by viewModels<SearchViewModel>()

  private val playerViewModel by viewModels<PlayerViewModel>()

  private lateinit var binding: FragmentFollowBinding

  private lateinit var easifyItemPagedListAdapter: EasifyItemListAdapter

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val root = view.findViewById<ConstraintLayout>(R.id.follow_fragment_root)
    DataBindingUtil.bind<FragmentFollowBinding>(root)?.apply {
      binding = this
    }
    setAdapters()
    setupObservers()
    setListeners()
  }

  private fun setListeners() {
    binding.search.onSearch = { input ->
      searchViewModel.search(SearchType.ARTIST, input)
    }
  }

  private fun setAdapters() {
    easifyItemPagedListAdapter = EasifyItemListAdapter(searchViewModel)
    binding.searchArtists.adapter = easifyItemPagedListAdapter
  }

  private fun setupObservers() {
    searchViewModel.event.observe(viewLifecycleOwner, EventObserver { event ->
      when (event) {
        SearchViewEvent.GetDevices -> getDevices()
        SearchViewEvent.Play -> playArtist()
        is SearchViewEvent.OnArtistClicked -> onArtistClicked(event.artist)
        is SearchViewEvent.OnListenIconClicked -> setClickedArtistUri(event.uri)
        is SearchViewEvent.NotifyArtistDataChanged -> onViewDataChange(event.artistList)
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
      playerViewModel.play()
    } ?: run { showOpenSpotifyWarning() }
  }

  private fun playArtist() {
    playerViewModel.play()
  }

  private fun getDevices() {
    playerViewModel.getDevices()
  }

  private fun onArtistClicked(artist: EasifyArtist) {
    val action = FollowFragmentDirections.actionFollowFragmentToArtistFragment(artist)
    findNavController().navigate(action)
  }

  private fun onViewDataChange(items: ArrayList<EasifyItem>) {
    easifyItemPagedListAdapter.submitList(items)
  }

  private fun setClickedArtistUri(uri: String) {
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
