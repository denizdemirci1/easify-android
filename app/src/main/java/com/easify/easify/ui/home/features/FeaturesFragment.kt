package com.easify.easify.ui.home.features

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.afollestad.materialdialogs.MaterialDialog
import com.easify.easify.R
import com.easify.easify.databinding.FragmentFeaturesBinding
import com.easify.easify.model.FeaturesResponse
import com.easify.easify.ui.base.BaseFragment
import com.easify.easify.util.EventObserver
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author: deniz.demirci
 * @date: 28.11.2020
 */

private const val TEMPO_DIVISOR = 250

@AndroidEntryPoint
class FeaturesFragment : BaseFragment(R.layout.fragment_features) {

  private val featuresViewModel by viewModels<FeaturesViewModel>()

  private lateinit var binding: FragmentFeaturesBinding

  private val args: FeaturesFragmentArgs by navArgs()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val root = view.findViewById<ConstraintLayout>(R.id.feature_fragment_root)
    DataBindingUtil.bind<FragmentFeaturesBinding>(root)?.apply {
      binding = this
      args.track?.let { track -> this.track = track }
    }
    showBottomNavigation(false)
    args.track?.id?.let(featuresViewModel::getAudioFeatures)
    setupObservers()
    setListeners()
  }

  private fun setListeners() {
    binding.search.setOnClickListener {

    }
  }

  private fun setupObservers() {
    featuresViewModel.event.observe(viewLifecycleOwner, EventObserver { event ->
      when (event) {
        is FeaturesViewEvent.OnFeaturesReceived -> setFeatures(event.features)
        is FeaturesViewEvent.ShowError -> showError(event.message)
      }
    })
  }

  private fun setFeatures(features: FeaturesResponse) {
    binding.danceability.setFeature(features.danceability)
    binding.energy.setFeature(features.energy)
    binding.speechiness.setFeature(features.speechiness)
    binding.acousticness.setFeature(features.acousticness)
    binding.instrumentalness.setFeature(features.instrumentalness)
    binding.liveness.setFeature(features.liveness)
    binding.valence.setFeature(features.valence)
    binding.tempo.setFeature(features.tempo / TEMPO_DIVISOR)
  }

  private fun showError(message: String) {
    MaterialDialog(requireContext()).show {
      title(R.string.dialog_error_title)
      message(text = message)
      positiveButton(R.string.dialog_ok)
    }
  }
}
