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
import com.easify.easify.R
import com.easify.easify.databinding.FragmentTopTracksBinding
import com.easify.easify.model.util.EasifyItem
import com.easify.easify.model.util.EasifyTrack
import com.easify.easify.ui.base.BaseFragment
import com.easify.easify.ui.common.adapter.EasifyItemPagedListAdapter
import com.easify.easify.ui.favorite.tracks.data.TopTracksDataSource
import com.easify.easify.ui.player.PlayerViewEvent
import com.easify.easify.ui.player.PlayerViewModel
import com.easify.easify.util.EventObserver
import com.spotify.sdk.android.authentication.AuthenticationResponse
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

  private lateinit var easifyItemPagedListAdapter: EasifyItemPagedListAdapter

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    DataBindingUtil.bind<FragmentTopTracksBinding>(topTracksRoot)?.apply {
      lifecycleOwner = this@TopTracksFragment.viewLifecycleOwner
      topTracksViewModel = this@TopTracksFragment.topTracksViewModel
      binding = this
    }
    setupObservers()
    setupPaging()
    setupTopArtistsAdapter()
    requestAds(binding.topTracksAdContainer)
  }

  private fun setupObservers() {
    topTracksViewModel.event.observe(viewLifecycleOwner, EventObserver { event ->
      when (event) {
        TopTracksViewEvent.Authenticate -> openSpotifyLoginActivity(::afterLogin)
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
        topTracksViewModel.saveToken(response)
        setupPaging()
      }
      AuthenticationResponse.Type.ERROR -> topTracksViewModel.clearToken()
      else -> Unit
    }
  }

  private fun setupTopArtistsAdapter() {
    easifyItemPagedListAdapter = EasifyItemPagedListAdapter(topTracksViewModel)
    binding.topTracksRecyclerView.adapter = easifyItemPagedListAdapter
  }

  private fun buildPagedListLiveData(): LiveData<PagedList<EasifyItem>> {
    return LivePagedListBuilder(
      object : DataSource.Factory<String, EasifyItem>() {
        override fun create(): DataSource<String, EasifyItem> {
          return TopTracksDataSource(args.timeRange, topTracksViewModel)
        }
      }, 20).build()
  }

  private fun getDevices() {
    playerViewModel.getDevices()
  }

  private fun playTrack() {
    playerViewModel.play(trackUris = topTracksViewModel.urisOfTracks, isTrack = true)
  }

  private fun setClickedTrackUri(uri: String) {
    playerViewModel.setUriToPlay(uri)
  }

  private fun onAddClicked(track: EasifyTrack) {
    val action = TopTracksFragmentDirections
      .actionTopTracksFragmentToAddTrackToPlaylistFragment2(track)
    findNavController().navigate(action)
  }

  private fun handleDeviceIdSet(deviceId: String?) {
    deviceId?.let {
      playTrack()
    } ?: run { showOpenSpotifyWarning() }
  }

  override fun onDestroyView() {
    topTracksViewModel.urisOfTracks.clear()
    super.onDestroyView()
  }
}
