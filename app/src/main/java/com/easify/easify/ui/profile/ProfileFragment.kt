package com.easify.easify.ui.profile

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.easify.easify.R
import com.easify.easify.databinding.FragmentProfileBinding
import com.easify.easify.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_profile.*

/**
 * @author: deniz.demirci
 * @date: 9/5/2020
 */

private const val APP_URL = "https://play.google.com/store/apps/details?id=com.easify.easify"
private const val APP_URI = "market://details?id=com.easify.easify"

@AndroidEntryPoint
class ProfileFragment : BaseFragment(R.layout.fragment_profile) {

  private val viewModel by viewModels<ProfileViewModel>()

  private lateinit var binding: FragmentProfileBinding

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    DataBindingUtil.bind<FragmentProfileBinding>(profileRoot)?.apply {
      lifecycleOwner = this@ProfileFragment.viewLifecycleOwner
      viewModel = this@ProfileFragment.viewModel
      binding = this
    }
    setListeners()
  }

  private fun setListeners() {
    binding.playlistsLayout.setOnClickListener { openPlaylistsFragment() }
    binding.followedArtistsLayout.setOnClickListener { openFollowedArtistsFragment() }
    binding.rateLayout.setOnClickListener { openPlayStore() }
  }

  private fun openPlaylistsFragment() {
    findNavController().navigate(R.id.playlistFragment)
  }

  private fun openFollowedArtistsFragment() {
    findNavController().navigate(R.id.followedArtistsFragment)
  }

  private fun openPlayStore() {
    try {
      startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(APP_URI)))
    } catch (e: ActivityNotFoundException) {
      startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(APP_URL)))
    }
  }
}
