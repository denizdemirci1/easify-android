package com.easify.easify.ui.home.features

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.easify.easify.R
import com.easify.easify.databinding.FragmentFeaturesBinding
import com.easify.easify.model.FeaturesResponse
import com.easify.easify.ui.base.BaseFragment
import com.easify.easify.util.EventObserver
import com.spotify.sdk.android.authentication.AuthenticationResponse
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author: deniz.demirci
 * @date: 28.11.2020
 */

@AndroidEntryPoint
class FeaturesFragment : BaseFragment(R.layout.fragment_features) {

  private val featuresViewModel by viewModels<FeaturesViewModel>()

  private lateinit var binding: FragmentFeaturesBinding

  private val args: FeaturesFragmentArgs by navArgs()

  private var features: FeaturesResponse? = null

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
      val action = FeaturesFragmentDirections.actionFeaturesFragmentToDiscoverFragment(
        features = features,
        track = args.track
      )
      findNavController().navigate(action)
    }
  }

  private fun setupObservers() {
    featuresViewModel.event.observe(viewLifecycleOwner, EventObserver { event ->
      when (event) {
        FeaturesViewEvent.Authenticate -> openSpotifyLoginActivity(::afterLogin)
        is FeaturesViewEvent.OnFeaturesReceived -> setFeatures(event.features)
        is FeaturesViewEvent.ShowError -> showError(event.message)
      }
    })
  }

  private fun afterLogin(
    responseType: AuthenticationResponse.Type?,
    response: String
  ) {
    when (responseType) {
      AuthenticationResponse.Type.TOKEN -> {
        featuresViewModel.saveToken(response)
        args.track?.id?.let(featuresViewModel::getAudioFeatures)
      }
      AuthenticationResponse.Type.ERROR -> featuresViewModel.clearToken()
      else -> Unit
    }
  }

  private fun setFeatures(features: FeaturesResponse) {
    this.features = features
    binding.features = features
  }
}
