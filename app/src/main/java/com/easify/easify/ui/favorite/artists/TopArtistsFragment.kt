package com.easify.easify.ui.favorite.artists

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.navArgs
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.afollestad.materialdialogs.MaterialDialog
import com.easify.easify.R
import com.easify.easify.databinding.FragmentTopArtistsBinding
import com.easify.easify.model.util.EasifyArtist
import com.easify.easify.model.util.EasifyItem
import com.easify.easify.ui.base.BaseFragment
import com.easify.easify.ui.common.adapter.EasifyItemPagedListAdapter
import com.easify.easify.ui.favorite.artists.data.TopArtistsDataSource
import com.easify.easify.ui.player.PlayerViewEvent
import com.easify.easify.ui.player.PlayerViewModel
import com.easify.easify.util.EventObserver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_top_artists.*

/**
 * @author: deniz.demirci
 * @date: 9/25/2020
 */

@AndroidEntryPoint
class TopArtistsFragment : BaseFragment(R.layout.fragment_top_artists) {

  private val topArtistsViewModel by viewModels<TopArtistsViewModel>()

  private val playerViewModel by viewModels<PlayerViewModel>()

  private lateinit var binding: FragmentTopArtistsBinding

  private val args: TopArtistsFragmentArgs by navArgs()

  private lateinit var easifyItemPagedListAdapter: EasifyItemPagedListAdapter

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    DataBindingUtil.bind<FragmentTopArtistsBinding>(topArtistsRoot)?.apply {
      lifecycleOwner = this@TopArtistsFragment.viewLifecycleOwner
      topArtistsViewModel = this@TopArtistsFragment.topArtistsViewModel
      binding = this
    }
    setupObservers()
    setupTopArtistsAdapter()
  }

  private fun setupObservers() {
    topArtistsViewModel.event.observe(viewLifecycleOwner, EventObserver { event ->
      when (event) {
        TopArtistsViewEvent.GetDevices -> getDevices()
        TopArtistsViewEvent.Play -> playArtist()
        is TopArtistsViewEvent.ListenIconClicked -> setClickedArtistUri(event.uri)
        is TopArtistsViewEvent.OpenArtistFragment -> openArtistFragment(event.artist)
        is TopArtistsViewEvent.ShowError -> showError(event.message)
      }
    })

    playerViewModel.event.observe(viewLifecycleOwner, EventObserver{ event ->
      when (event) {
        is PlayerViewEvent.DeviceIdSet -> handleDeviceIdSet(event.deviceId)
        PlayerViewEvent.ShowOpenSpotifyWarning -> showOpenSpotifyWarning()
      }
    })

    buildPagedListLiveData().observe(viewLifecycleOwner, { list ->
      easifyItemPagedListAdapter.submitList(list)
    })
  }

  private fun setupTopArtistsAdapter() {
    easifyItemPagedListAdapter = EasifyItemPagedListAdapter(topArtistsViewModel)
    binding.topArtistsRecyclerView.adapter = easifyItemPagedListAdapter
  }

  private fun buildPagedListLiveData(): LiveData<PagedList<EasifyItem>> {
    return LivePagedListBuilder(
      object : DataSource.Factory<String, EasifyItem>() {
        override fun create(): DataSource<String, EasifyItem> {
          return TopArtistsDataSource(args.timeRange, topArtistsViewModel)
        }
      }, 20).build()
  }

  private fun handleDeviceIdSet(deviceId: String?) {
    deviceId?.let {
      playerViewModel.play()
    } ?: run { showOpenSpotifyWarning() }
  }

  private fun setClickedArtistUri(uri: String) {
    playerViewModel.setUriToPlay(uri)
  }

  private fun playArtist() {
    playerViewModel.play()
  }

  private fun getDevices() {
    playerViewModel.getDevices()
  }

  private fun openArtistFragment(artist: EasifyArtist) {
    // TODO: open
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
