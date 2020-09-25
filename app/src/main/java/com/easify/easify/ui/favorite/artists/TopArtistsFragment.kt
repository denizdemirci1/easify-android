package com.easify.easify.ui.favorite.artists

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.easify.easify.R
import com.easify.easify.databinding.FragmentTopArtistsBinding
import com.easify.easify.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_top_artists.*

/**
 * @author: deniz.demirci
 * @date: 9/25/2020
 */

@AndroidEntryPoint
class TopArtistsFragment : BaseFragment(R.layout.fragment_top_artists) {

  private val viewModel by viewModels<TopArtistsViewModel>()

  private lateinit var binding: FragmentTopArtistsBinding

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    DataBindingUtil.bind<FragmentTopArtistsBinding>(topArtistsRoot)?.apply {
      lifecycleOwner = this@TopArtistsFragment.viewLifecycleOwner
      viewModel = this@TopArtistsFragment.viewModel
      binding = this
    }
  }
}
