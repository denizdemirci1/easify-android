package com.easify.easify.ui.profile.follows.followed

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.easify.easify.R
import com.easify.easify.databinding.FragmentFollowedArtistsBinding
import com.easify.easify.model.util.EasifyArtist
import com.easify.easify.model.util.EasifyItem
import com.easify.easify.ui.base.BaseFragment
import com.easify.easify.ui.common.adapter.EasifyItemPagedListAdapter
import com.easify.easify.ui.player.PlayerViewEvent
import com.easify.easify.ui.profile.follows.followed.data.FollowedArtistsDataSource
import com.easify.easify.util.EventObserver
import com.easify.easify.ui.player.PlayerViewModel
import com.spotify.sdk.android.authentication.AuthenticationResponse
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

  private lateinit var easifyItemPagedListAdapter: EasifyItemPagedListAdapter

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    showBottomNavigation(false)
    DataBindingUtil.bind<FragmentFollowedArtistsBinding>(followed_artists_root)?.apply {
      lifecycleOwner = this@FollowedArtistsFragment.viewLifecycleOwner
      viewModel = this@FollowedArtistsFragment.followedArtistsViewModel
      binding = this
    }
    setupObservers()
    setupFollowedArtistsAdapter()
    setupListeners()
  }

  private fun setupListeners() {
    binding.followNew.setOnClickListener {
      findNavController().navigate(R.id.followFragment)
    }
  }

  private fun setupObservers() {
    followedArtistsViewModel.event.observe(viewLifecycleOwner, EventObserver { event ->
      when (event) {
        FollowedArtistsViewEvent.Authenticate -> openSpotifyLoginActivity(::afterLogin)
        FollowedArtistsViewEvent.GetDevices -> getDevices()
        FollowedArtistsViewEvent.Play -> playArtist()
        is FollowedArtistsViewEvent.ListenIconClicked -> setClickedArtistUri(event.uri)
        is FollowedArtistsViewEvent.ShowError -> showError(event.message)
        is FollowedArtistsViewEvent.OpenArtistFragment -> openArtistFragment(event.artist)
      }
    })

    playerViewModel.event.observe(viewLifecycleOwner, EventObserver{ event ->
      when (event) {
        is PlayerViewEvent.DeviceIdSet -> handleDeviceIdSet(event.deviceId)
        PlayerViewEvent.ShowOpenSpotifyWarning -> showOpenSpotifyWarning()
        PlayerViewEvent.Authenticate -> openSpotifyLoginActivity(::afterLogin)
      }
    })

    buildPagedListLiveData().observe(viewLifecycleOwner, { list ->
      easifyItemPagedListAdapter.submitList(list)
    })
  }

  private fun afterLogin(
    responseType: AuthenticationResponse.Type?,
    response: String
  ) {
    when (responseType) {
      AuthenticationResponse.Type.TOKEN -> followedArtistsViewModel.saveToken(response)
      AuthenticationResponse.Type.ERROR -> followedArtistsViewModel.clearToken()
      else -> Unit
    }
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

  private fun setupFollowedArtistsAdapter() {
    easifyItemPagedListAdapter = EasifyItemPagedListAdapter(followedArtistsViewModel)
    binding.followedArtistsRecyclerView.adapter = easifyItemPagedListAdapter
  }

  private fun buildPagedListLiveData(): LiveData<PagedList<EasifyItem>> {
    return LivePagedListBuilder(
      object : DataSource.Factory<String, EasifyItem>() {
        override fun create(): DataSource<String, EasifyItem> {
          return FollowedArtistsDataSource(followedArtistsViewModel)
        }
      }, 30).build()
  }

  private fun getDevices() {
    playerViewModel.getDevices()
  }

  private fun openArtistFragment(artist: EasifyArtist) {
    val action =
      FollowedArtistsFragmentDirections.actionFollowedArtistsFragmentToArtistFragment(artist)
    findNavController().navigate(action)
  }

  override fun onDestroy() {
    showBottomNavigation(true)
    super.onDestroy()
  }

}
