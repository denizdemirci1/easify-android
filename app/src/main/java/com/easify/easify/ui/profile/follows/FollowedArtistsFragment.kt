package com.easify.easify.ui.profile.follows

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.easify.easify.R
import com.easify.easify.databinding.FragmentFollowedArtistsBinding
import com.easify.easify.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_followed_artists.*

/**
 * @author: deniz.demirci
 * @date: 9/29/2020
 */

@AndroidEntryPoint
class FollowedArtistsFragment : BaseFragment(R.layout.fragment_followed_artists) {

  private val viewModel by viewModels<FollowedArtistsViewModel>()

  private lateinit var binding: FragmentFollowedArtistsBinding

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    DataBindingUtil.bind<FragmentFollowedArtistsBinding>(followed_artists_root)?.apply {
      lifecycleOwner = this@FollowedArtistsFragment.viewLifecycleOwner
      viewModel = this@FollowedArtistsFragment.viewModel
      binding = this
    }
  }
}
