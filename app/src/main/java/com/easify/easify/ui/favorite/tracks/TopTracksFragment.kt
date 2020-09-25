package com.easify.easify.ui.favorite.tracks

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.easify.easify.R
import com.easify.easify.databinding.FragmentTopTracksBinding
import com.easify.easify.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_top_tracks.*

/**
 * @author: deniz.demirci
 * @date: 9/25/2020
 */

@AndroidEntryPoint
class TopTracksFragment : BaseFragment(R.layout.fragment_top_tracks) {

  private val viewModel by viewModels<TopTracksViewModel>()

  private lateinit var binding: FragmentTopTracksBinding

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    DataBindingUtil.bind<FragmentTopTracksBinding>(topTracksRoot)?.apply {
      lifecycleOwner = this@TopTracksFragment.viewLifecycleOwner
      viewModel = this@TopTracksFragment.viewModel
      binding = this
    }
  }
}
