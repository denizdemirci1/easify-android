package com.easify.easify.ui.home.main

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.easify.easify.R
import com.easify.easify.databinding.FragmentHomeBinding
import com.easify.easify.model.SearchType
import com.easify.easify.model.util.EasifyItem
import com.easify.easify.model.util.EasifyItemType
import com.easify.easify.model.util.EasifyTrack
import com.easify.easify.ui.base.BaseFragment
import com.easify.easify.ui.common.adapter.EasifyItemListAdapter
import com.easify.easify.ui.extensions.requestInAppReviewDialog
import com.easify.easify.ui.player.PlayerViewEvent
import com.easify.easify.ui.player.PlayerViewModel
import com.easify.easify.ui.search.SearchViewEvent
import com.easify.easify.ui.search.SearchViewModel
import com.easify.easify.util.EventObserver
import com.spotify.sdk.android.authentication.AuthenticationResponse
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author: deniz.demirci
 * @date: 9/4/2020
 */

@AndroidEntryPoint
class HomeFragment : BaseFragment(R.layout.fragment_home) {

  private val searchViewModel by viewModels<SearchViewModel>()

  private val playerViewModel by viewModels<PlayerViewModel>()

  private val homeViewModel by viewModels<HomeViewModel>()

  private lateinit var binding: FragmentHomeBinding

  private lateinit var searchedEasifyItemAdapter: EasifyItemListAdapter
  private lateinit var featuredEasifyTracksAdapter: EasifyItemListAdapter
  private lateinit var featuredEasifyArtistsAdapter: EasifyItemListAdapter

  private var clickedItemType = EasifyItemType.TRACK

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val root = view.findViewById<ConstraintLayout>(R.id.home_fragment_root)
    DataBindingUtil.bind<FragmentHomeBinding>(root)?.apply {
      binding = this
      searchedTracksVisibility = false
      featuredTracksVisibility = false
      featuredArtistsVisibility = false
    }
    showBottomNavigation(true)
    setAdapters()
    setupObservers()
    setListeners()
    homeViewModel.getFeaturedItems()
    activity?.requestInAppReviewDialog(homeViewModel.userManager)
  }

  private fun setListeners() {
    binding.search.onSearch = { input ->
      binding.searchedTracksVisibility = true
      searchViewModel.search(SearchType.TRACK, input)
    }
    binding.search.onSearchCleared = {
      binding.searchedTracksVisibility = false
    }
  }

  private fun setAdapters() {
    searchedEasifyItemAdapter = EasifyItemListAdapter(searchViewModel)
    binding.searchedTracks.adapter = searchedEasifyItemAdapter

    featuredEasifyTracksAdapter = EasifyItemListAdapter(homeViewModel, horizontal = true)
    binding.featuredTracks.adapter = featuredEasifyTracksAdapter

    featuredEasifyArtistsAdapter = EasifyItemListAdapter(homeViewModel, horizontal = true)
    binding.featuredArtists.adapter = featuredEasifyArtistsAdapter
  }

  private fun setupObservers() {
    searchViewModel.event.observe(viewLifecycleOwner, EventObserver { event ->
      when (event) {
        SearchViewEvent.Authenticate -> openSpotifyLoginActivity(::afterLogin)
        SearchViewEvent.GetDevices -> getDevices()
        SearchViewEvent.Play -> playTrack()
        is SearchViewEvent.OnTrackClicked -> onTrackClicked(event.track)
        is SearchViewEvent.OnListenIconClicked -> setClickedTrackUri(event.uri)
        is SearchViewEvent.OnAddIconClicked -> onAddIconClicked(event.track)
        is SearchViewEvent.NotifyTrackDataChanged -> {
          onAdapterDataChanged(searchedEasifyItemAdapter, event.trackList)
        }
        is SearchViewEvent.ShowError -> showError(event.message)
        else -> Unit
      }
    })

    homeViewModel.event.observe(viewLifecycleOwner, EventObserver { event ->
      when (event) {
        HomeViewEvent.Authenticate -> openSpotifyLoginActivity(::afterLogin)
        is HomeViewEvent.OnItemClicked -> {
          clickedItemType = event.item.type
          searchViewModel.onListenIconClick(event.item)
        }
        is HomeViewEvent.OnFeaturedTracksReceived -> {
          binding.featuredTracksVisibility = true
          onAdapterDataChanged(featuredEasifyTracksAdapter, event.tracks)
        }
        is HomeViewEvent.OnFeaturedArtistsReceived -> {
          binding.featuredArtistsVisibility = true
          onAdapterDataChanged(featuredEasifyArtistsAdapter, event.artists)
        }
        is HomeViewEvent.ShowError -> showError(event.message)
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

  private fun afterLogin(
    responseType: AuthenticationResponse.Type?,
    response: String
  ) {
    when (responseType) {
      AuthenticationResponse.Type.TOKEN -> {
        homeViewModel.saveToken(response)
        homeViewModel.getFeaturedItems()
      }
      AuthenticationResponse.Type.ERROR -> homeViewModel.clearToken()
      else -> Unit
    }
  }


  private fun handleDeviceIdSet(deviceId: String?) {
    deviceId?.let {
      playTrack()
    } ?: run { showOpenSpotifyWarning() }
  }

  private fun playTrack() {
    when (clickedItemType) {
      EasifyItemType.TRACK -> playerViewModel.play(isTrack = true)
      else -> playerViewModel.play()
    }
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

  private fun onAdapterDataChanged(adapter: EasifyItemListAdapter, items: ArrayList<EasifyItem>) {
    adapter.submitList(items)
  }

  private fun setClickedTrackUri(uri: String) {
    playerViewModel.setUriToPlay(uri)
  }
}
