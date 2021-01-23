package com.easify.easify.ui.home.recommendations

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.easify.easify.R
import com.easify.easify.databinding.FragmentRecommendationsBinding
import com.easify.easify.model.util.EasifyTrack
import com.easify.easify.ui.base.BaseFragment
import com.easify.easify.ui.common.adapter.EasifyItemListAdapter
import com.easify.easify.ui.player.PlayerViewEvent
import com.easify.easify.ui.player.PlayerViewModel
import com.easify.easify.util.DateTimeHelper
import com.easify.easify.util.EventObserver
import com.google.android.material.snackbar.Snackbar
import com.spotify.sdk.android.authentication.AuthenticationResponse
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author: deniz.demirci
 * @date: 29.11.2020
 */

@AndroidEntryPoint
class RecommendationsFragment : BaseFragment(R.layout.fragment_recommendations) {

  private val recommendationsViewModel by viewModels<RecommendationsViewModel>()

  private val playerViewModel by viewModels<PlayerViewModel>()

  private lateinit var binding: FragmentRecommendationsBinding

  private val args: RecommendationsFragmentArgs by navArgs()

  private lateinit var easifyItemListAdapter: EasifyItemListAdapter

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val root = view.findViewById<ConstraintLayout>(R.id.recommendations_fragment_root)
    DataBindingUtil.bind<FragmentRecommendationsBinding>(root)?.apply {
      binding = this
    }
    setAdapter()
    setupObservers()
    setListeners()
    args.features?.let(recommendationsViewModel::getRecommendations)
    requestAds(binding.recommendationsAdContainer)
  }

  private fun setAdapter() {
    easifyItemListAdapter = EasifyItemListAdapter(recommendationsViewModel)
    binding.recommendedTracks.adapter = easifyItemListAdapter
  }

  private fun setListeners() {
    binding.createPlaylist.setOnClickListener {
      it.isEnabled = false
      it.alpha = 0.3f
      createPlaylist()
    }
  }

  private fun createPlaylist() {
    args.track?.let { track ->
      recommendationsViewModel.createPlaylist(
        name = getString(R.string.fragment_recommendations_create_playlist_name, track.name),
        description = getString(
          R.string.fragment_recommendations_create_playlist_desc,
          DateTimeHelper.getToday()
        )
      )
    }
  }

  private fun setupObservers() {
    recommendationsViewModel.event.observe(viewLifecycleOwner, EventObserver { event ->
      when (event) {
        RecommendationsViewEvent.Authenticate -> openSpotifyLoginActivity(::afterLogin)
        RecommendationsViewEvent.GetDevices -> getDevices()
        RecommendationsViewEvent.Play -> playTrack()
        is RecommendationsViewEvent.TrackClicked -> setClickedTrackUri(event.uri)
        is RecommendationsViewEvent.AddIconClicked -> onAddClicked(event.track)
        is RecommendationsViewEvent.OnRecommendationsReceived -> {
          easifyItemListAdapter.submitList(event.tracks)
        }
        RecommendationsViewEvent.ShowUserIdNotFoundError -> {
          binding.createPlaylist.isEnabled = true
          showError(getString(R.string.fragment_create_playlist_user_id_not_found_error))
        }
        is RecommendationsViewEvent.OnCreatePlaylistResponse -> showSnackBar(event.isSuccessful)
        is RecommendationsViewEvent.ShowError -> {
          binding.createPlaylist.isEnabled = true
          showError(event.message)
        }
      }
    })

    playerViewModel.event.observe(viewLifecycleOwner, EventObserver{ event ->
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
      AuthenticationResponse.Type.TOKEN -> recommendationsViewModel.saveToken(response)
      AuthenticationResponse.Type.ERROR -> recommendationsViewModel.clearToken()
      else -> Unit
    }
  }

  private fun setClickedTrackUri(uri: String) {
    playerViewModel.setUriToPlay(uri)
  }

  private fun playTrack() {
    playerViewModel.play(trackUris = recommendationsViewModel.urisOfTracks, isTrack = true)
  }

  private fun getDevices() {
    playerViewModel.getDevices()
  }

  private fun onAddClicked(track: EasifyTrack) {
    val action = RecommendationsFragmentDirections
      .actionRecommendationsFragmentToAddTrackToPlaylistFragment3(track)
    findNavController().navigate(action)
  }

  private fun handleDeviceIdSet(deviceId: String?) {
    deviceId?.let {
      playTrack()
    } ?: run { showOpenSpotifyWarning() }
  }

  private fun showSnackBar(isSuccess: Boolean) {
    binding.createPlaylist.isEnabled = !isSuccess
    view?.let {
      val message =
        if (isSuccess)
          getString(R.string.fragment_recommendations_create_playlist_succeeded)
        else
          getString(R.string.fragment_recommendations_create_playlist_failed)

      val snackbar = Snackbar.make(it, message, Snackbar.LENGTH_LONG)
      context?.let { safeContext ->
        snackbar.view.background = ContextCompat.getDrawable(safeContext, R.drawable.bg_snackbar)
      }
      val params = snackbar.view.layoutParams as ViewGroup.MarginLayoutParams
      params.setMargins(24, 24, 24, 48)
      snackbar.view.layoutParams = params
      snackbar.show()
    }
  }

  override fun onDestroyView() {
    recommendationsViewModel.urisOfTracks.clear()
    super.onDestroyView()
  }
}
