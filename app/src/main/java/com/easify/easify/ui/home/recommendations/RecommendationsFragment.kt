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
import com.afollestad.materialdialogs.MaterialDialog
import com.easify.easify.R
import com.easify.easify.databinding.FragmentRecommendationsBinding
import com.easify.easify.model.Track
import com.easify.easify.ui.base.BaseFragment
import com.easify.easify.ui.home.recommendations.data.RecommendedTracksAdapter
import com.easify.easify.ui.player.PlayerViewEvent
import com.easify.easify.ui.player.PlayerViewModel
import com.easify.easify.util.DateTimeHelper
import com.easify.easify.util.EventObserver
import com.google.android.material.snackbar.Snackbar
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

  private lateinit var recommendedTracksAdapter: RecommendedTracksAdapter

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
  }

  private fun setAdapter() {
    recommendedTracksAdapter = RecommendedTracksAdapter(recommendationsViewModel)
    binding.recommendedTracks.adapter = recommendedTracksAdapter
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
        RecommendationsViewEvent.GetDevices -> getDevices()
        RecommendationsViewEvent.Play -> playTrack()
        is RecommendationsViewEvent.TrackClicked -> setClickedTrackUri(event.uri)
        is RecommendationsViewEvent.AddIconClicked -> onAddClicked(event.track)
        is RecommendationsViewEvent.OnRecommendationsReceived -> {
          recommendedTracksAdapter.submitList(event.tracks)
        }
        RecommendationsViewEvent.ShowUserIdNotFoundError -> {
          showError(getString(R.string.fragment_create_playlist_user_id_not_found_error))
        }
        is RecommendationsViewEvent.OnCreatePlaylistResponse -> showSnackBar(event.isSuccessful)
        is RecommendationsViewEvent.ShowError -> showError(event.message)
      }
    })

    playerViewModel.event.observe(viewLifecycleOwner, EventObserver{ event ->
      when (event) {
        is PlayerViewEvent.DeviceIdSet -> handleDeviceIdSet(event.deviceId)
        PlayerViewEvent.ShowOpenSpotifyWarning -> showOpenSpotifyWarning()
      }
    })
  }

  private fun setClickedTrackUri(uri: String) {
    playerViewModel.setUriToPlay(uri)
  }

  private fun playTrack() {
    playerViewModel.play(isTrack = true)
  }

  private fun getDevices() {
    playerViewModel.getDevices()
  }

  private fun onAddClicked(track: Track) {
    val action = RecommendationsFragmentDirections
      .actionRecommendationsFragmentToAddTrackToPlaylistFragment3(track)
    findNavController().navigate(action)
  }

  private fun handleDeviceIdSet(deviceId: String?) {
    deviceId?.let {
      playerViewModel.play(isTrack = true)
    } ?: run { showOpenSpotifyWarning() }
  }

  private fun showError(message: String) {
    binding.createPlaylist.isEnabled = true
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
}
