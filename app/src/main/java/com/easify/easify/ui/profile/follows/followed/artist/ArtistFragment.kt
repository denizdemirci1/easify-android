package com.easify.easify.ui.profile.follows.followed.artist

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.afollestad.materialdialogs.MaterialDialog
import com.easify.easify.R
import com.easify.easify.databinding.FragmentArtistBinding
import com.easify.easify.ui.base.BaseFragment
import com.easify.easify.util.EventObserver
import com.easify.easify.ui.player.PlayerViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_artist.*

/**
 * @author: deniz.demirci
 * @date: 29.10.2020
 */

@AndroidEntryPoint
class ArtistFragment : BaseFragment(R.layout.fragment_artist) {

  private val artistViewModel by viewModels<ArtistViewModel>()

  private val playerViewModel by viewModels<PlayerViewModel>()

  private lateinit var binding: FragmentArtistBinding

  private val args: ArtistFragmentArgs by navArgs()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    DataBindingUtil.bind<FragmentArtistBinding>(artist_root)?.apply {
      lifecycleOwner = this@ArtistFragment.viewLifecycleOwner
      artistViewModel = this@ArtistFragment.artistViewModel
      artist = this@ArtistFragment.args.artist
      binding = this
    }
    artistViewModel.setArtist(args.artist)
    artistViewModel.fetchFollowedArtists()
    setupObservers()
  }

  private fun setupObservers() {
    artistViewModel.event.observe(viewLifecycleOwner, EventObserver{ event ->
      when (event) {
        is ArtistViewEvent.ShowSnackbar -> showSnackbar(event.followStatus)
        is ArtistViewEvent.ShowError -> showError(event.message)
      }
    })
  }

  private fun showError(message: String) {
    MaterialDialog(requireContext()).show {
      title(R.string.dialog_error_title)
      message(text = message)
      positiveButton(R.string.dialog_ok)
    }
  }

  private fun showSnackbar(followStatus: ArtistViewModel.FollowStatus) {
    val message = when (followStatus) {
      ArtistViewModel.FollowStatus.FOLLOW -> {
        getString(R.string.fragment_artist_snackbar_followed, args.artist?.name)
      }
      ArtistViewModel.FollowStatus.UNFOLLOW -> {
        getString(R.string.fragment_artist_snackbar_unfollowed, args.artist?.name)
      }
    }
    view?.let {
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
