package com.easify.easify.ui.profile.follows

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.afollestad.materialdialogs.MaterialDialog
import com.easify.easify.R
import com.easify.easify.databinding.FragmentFollowedArtistsBinding
import com.easify.easify.model.Artist
import com.easify.easify.ui.base.BaseFragment
import com.easify.easify.ui.profile.follows.adapter.FollowedArtistsAdapter
import com.easify.easify.ui.profile.follows.data.FollowedArtistsDataSource
import com.easify.easify.util.EventObserver
import com.easify.easify.util.viewmodels.PlayerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_followed_artists.*

/**
 * @author: deniz.demirci
 * @date: 9/29/2020
 */

@AndroidEntryPoint
class FollowedArtistsFragment : BaseFragment(R.layout.fragment_followed_artists) {

  private val followedArtistsViewModel by viewModels<FollowedArtistsViewModel>()

  private val playerViewModel by viewModels<PlayerViewModel>()

  private lateinit var binding: FragmentFollowedArtistsBinding

  private lateinit var followedArtistsAdapter: FollowedArtistsAdapter

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    DataBindingUtil.bind<FragmentFollowedArtistsBinding>(followed_artists_root)?.apply {
      lifecycleOwner = this@FollowedArtistsFragment.viewLifecycleOwner
      viewModel = this@FollowedArtistsFragment.followedArtistsViewModel
      binding = this
    }
    setupObservers()
    setupFollowedArtistsAdapter()
  }

  private fun setupObservers() {
    followedArtistsViewModel.event.observe(viewLifecycleOwner, EventObserver { event ->
      when (event) {
        FollowedArtistsViewEvent.GetDevices -> getDevices()
        FollowedArtistsViewEvent.ShowOpenSpotifyWarning -> showOpenSpotifyWarning()
        is FollowedArtistsViewEvent.ShowError -> showError(event.message)
        is FollowedArtistsViewEvent.OpenArtistFragment -> openArtistFragment(event.artist)
      }
    })

    playerViewModel.deviceId.observe(viewLifecycleOwner, { deviceId ->
      deviceId?.let { _ ->
        followedArtistsViewModel.playSongsOfClickedArtist()
      } ?: run { showOpenSpotifyWarning() }
    })

    buildPagedListLiveData().observe(viewLifecycleOwner, { list ->
      followedArtistsAdapter.submitList(list)
    })
  }

  private fun setupFollowedArtistsAdapter() {
    followedArtistsAdapter = FollowedArtistsAdapter(followedArtistsViewModel)
    binding.followedArtistsRecyclerView.adapter = followedArtistsAdapter
  }

  private fun buildPagedListLiveData(): LiveData<PagedList<Artist>> {
    return LivePagedListBuilder(
      object : DataSource.Factory<String, Artist>() {
        override fun create(): DataSource<String, Artist> {
          return FollowedArtistsDataSource(followedArtistsViewModel)
        }
      }, 30).build()
  }

  private fun getDevices() {
    playerViewModel.getDevices()
  }

  private fun openArtistFragment(artist: Artist) {
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
