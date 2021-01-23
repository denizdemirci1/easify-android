package com.easify.easify.ui.history

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.adcolony.sdk.AdColonyAdView
import com.easify.easify.R
import com.easify.easify.databinding.FragmentHistoryBinding
import com.easify.easify.model.util.EasifyItem
import com.easify.easify.model.util.EasifyTrack
import com.easify.easify.ui.base.BaseFragment
import com.easify.easify.ui.common.adapter.EasifyItemPagedListAdapter
import com.easify.easify.ui.history.data.HistoryDataSource
import com.easify.easify.ui.player.PlayerViewEvent
import com.easify.easify.ui.player.PlayerViewModel
import com.easify.easify.util.EventObserver
import com.spotify.sdk.android.authentication.AuthenticationResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_history.*


/**
 * @author: deniz.demirci
 * @date: 9/5/2020
 */

@AndroidEntryPoint
class HistoryFragment : BaseFragment(R.layout.fragment_history) {

  private val historyViewModel by viewModels<HistoryViewModel>()

  private val playerViewModel by viewModels<PlayerViewModel>()

  private lateinit var binding: FragmentHistoryBinding

  private lateinit var easifyItemPagedListAdapter: EasifyItemPagedListAdapter

  private var adColonyAdView: AdColonyAdView? = null

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    DataBindingUtil.bind<FragmentHistoryBinding>(historyRoot)?.apply {
      lifecycleOwner = this@HistoryFragment.viewLifecycleOwner
      historyViewModel = this@HistoryFragment.historyViewModel
      binding = this
    }
    setupObservers()
    setUpPaging()
    setupHistoryAdapter()
    requestAds(binding.historyAdContainer)
  }

  private fun setupObservers() {
    historyViewModel.event.observe(viewLifecycleOwner, EventObserver { event ->
      when (event) {
        HistoryViewEvent.Authenticate -> openSpotifyLoginActivity(::afterLogin)
        HistoryViewEvent.GetDevices -> getDevices()
        HistoryViewEvent.Play -> playTrack()
        is HistoryViewEvent.TrackClicked -> setClickedTrackUri(event.uri)
        is HistoryViewEvent.AddIconClicked -> onAddClicked(event.track)
        is HistoryViewEvent.ShowError -> showError(event.message)
      }
    })

    playerViewModel.event.observe(viewLifecycleOwner, EventObserver { event ->
      when (event) {
        is PlayerViewEvent.DeviceIdSet -> handleDeviceIdSet(event.deviceId)
        is PlayerViewEvent.ForceOpenSpotify -> openUriWithSpotify(event.uri)
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
        historyViewModel.saveToken(response)
        setUpPaging()
      }
      AuthenticationResponse.Type.ERROR -> historyViewModel.clearToken()
      else -> Unit
    }
  }

  private fun setUpPaging() {
    buildPagedListLiveData().observe(viewLifecycleOwner, { list ->
      easifyItemPagedListAdapter.submitList(list)
    })
  }

  private fun setupHistoryAdapter() {
    easifyItemPagedListAdapter = EasifyItemPagedListAdapter(historyViewModel)
    binding.tracksRecyclerView.adapter = easifyItemPagedListAdapter
  }

  private fun buildPagedListLiveData(): LiveData<PagedList<EasifyItem>> {
    return LivePagedListBuilder(
      object : DataSource.Factory<String, EasifyItem>() {
        override fun create(): DataSource<String, EasifyItem> {
          return HistoryDataSource(historyViewModel)
        }
      }, 30
    ).build()
  }

  private fun setClickedTrackUri(uri: String) {
    playerViewModel.setUriToPlay(uri)
  }

  private fun playTrack() {
    playerViewModel.play(trackUris = historyViewModel.urisOfTracks, isTrack = true)
  }

  private fun getDevices() {
    playerViewModel.getDevices()
  }

  private fun handleDeviceIdSet(deviceId: String?) {
    deviceId?.let {
      playTrack()
    } ?: run { showOpenSpotifyWarning() }
  }

  private fun onAddClicked(track: EasifyTrack) {
    val action = HistoryFragmentDirections.actionHistoryFragmentToAddTrackToPlaylistFragment(track)
    findNavController().navigate(action)
  }

  override fun onDestroyView() {
    historyViewModel.urisOfTracks.clear()
    adColonyAdView?.destroy()
    super.onDestroyView()
  }
}
