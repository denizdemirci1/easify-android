package com.easify.easify.ui.home.discover

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.easify.easify.R
import com.easify.easify.databinding.FragmentDiscoverBinding
import com.easify.easify.model.FeaturesResponse
import com.easify.easify.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author: deniz.demirci
 * @date: 29.11.2020
 */

@AndroidEntryPoint
class DiscoverFragment : BaseFragment(R.layout.fragment_discover) {

  private lateinit var binding: FragmentDiscoverBinding

  private val args: DiscoverFragmentArgs by navArgs()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val root = view.findViewById<ConstraintLayout>(R.id.discover_fragment_root)
    DataBindingUtil.bind<FragmentDiscoverBinding>(root)?.apply {
      binding = this
      binding.features = args.features
      args.track?.let { track -> this.track = track }
    }
    setListeners()
  }

  private fun setListeners() {
    binding.discover.setOnClickListener {
      val action = DiscoverFragmentDirections
        .actionDiscoverFragmentToRecommendationsFragment(getDesiredFeatures(), args.track)
      findNavController().navigate(action)
    }
  }

  private fun getDesiredFeatures() = FeaturesResponse(
    binding.danceability.getProgress(),
    binding.energy.getProgress(),
    binding.speechiness.getProgress(),
    binding.acousticness.getProgress(),
    binding.instrumentalness.getProgress(),
    binding.liveness.getProgress(),
    binding.valence.getProgress(),
    binding.tempo.getProgress(),
    args.track?.id.orEmpty()
  )
}
