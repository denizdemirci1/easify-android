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
import com.adcolony.sdk.AdColony
import com.adcolony.sdk.AdColonyAdSize
import com.adcolony.sdk.AdColonyAdView
import com.adcolony.sdk.AdColonyAdViewListener
import com.afollestad.materialdialogs.MaterialDialog
import com.easify.easify.BuildConfig
import com.easify.easify.R
import com.easify.easify.databinding.FragmentHistoryBinding
import com.easify.easify.model.History
import com.easify.easify.model.Track
import com.easify.easify.ui.base.BaseFragment
import com.easify.easify.ui.history.adapter.HistoryAdapter
import com.easify.easify.ui.history.data.HistoryDataSource
import com.easify.easify.ui.player.PlayerViewEvent
import com.easify.easify.ui.player.PlayerViewModel
import com.easify.easify.util.EventObserver
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

  private lateinit var historyAdapter: HistoryAdapter

  private var adColonyAdView: AdColonyAdView? = null

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    DataBindingUtil.bind<FragmentHistoryBinding>(historyRoot)?.apply {
      lifecycleOwner = this@HistoryFragment.viewLifecycleOwner
      historyViewModel = this@HistoryFragment.historyViewModel
      binding = this
    }
    requestAds()
    setupObservers()
    setupHistoryAdapter()
  }

  private fun requestAds() {
    AdColony.configure(
      requireActivity(),
      BuildConfig.ADCOLONY_APP_ID,
      BuildConfig.ADCOLONY_BANNER_AD_ZONE_ID
    )
    val listener: AdColonyAdViewListener = object : AdColonyAdViewListener() {
      override fun onRequestFilled(ad: AdColonyAdView) {
        adColonyAdView = ad
        binding.historyAdContainer.addView(ad)
      }
    }

    AdColony.requestAdView(BuildConfig.ADCOLONY_BANNER_AD_ZONE_ID, listener, AdColonyAdSize.BANNER)
  }

  private fun setupObservers() {
    historyViewModel.event.observe(viewLifecycleOwner, EventObserver { event ->
      when (event) {
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
        PlayerViewEvent.ShowOpenSpotifyWarning -> showOpenSpotifyWarning()
      }
    })

    buildPagedListLiveData().observe(viewLifecycleOwner, { list ->
      historyAdapter.submitList(list)
    })
  }

  private fun setupHistoryAdapter() {
    historyAdapter = HistoryAdapter(historyViewModel)
    binding.tracksRecyclerView.adapter = historyAdapter
  }

  private fun buildPagedListLiveData(): LiveData<PagedList<History>> {
    return LivePagedListBuilder(
      object : DataSource.Factory<String, History>() {
        override fun create(): DataSource<String, History> {
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

  private fun onAddClicked(track: Track) {
    val action = HistoryFragmentDirections.actionHistoryFragmentToAddTrackToPlaylistFragment(track)
    findNavController().navigate(action)
  }

  override fun onDestroyView() {
    historyViewModel.urisOfTracks.clear()
    adColonyAdView?.destroy()
    super.onDestroyView()
  }
}
