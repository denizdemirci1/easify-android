package com.easify.easify.ui.favorite

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.adcolony.sdk.AdColony
import com.adcolony.sdk.AdColonyInterstitial
import com.adcolony.sdk.AdColonyInterstitialListener
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.easify.easify.BuildConfig
import com.easify.easify.R
import com.easify.easify.databinding.FragmentFavoriteBinding
import com.easify.easify.ui.base.BaseFragment
import com.easify.easify.ui.extensions.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_favorite.*

/**
 * @author: deniz.demirci
 * @date: 9/5/2020
 */

@AndroidEntryPoint
class FavoriteFragment : BaseFragment(R.layout.fragment_favorite) {

  private val viewModel by viewModels<FavoriteViewModel>()

  private lateinit var binding: FragmentFavoriteBinding

  private lateinit var longTerm: Pair<String, String>
  private lateinit var mediumTerm: Pair<String, String>
  private lateinit var shortTerm: Pair<String, String>
  private lateinit var artistKey: String
  private lateinit var trackKey: String

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    DataBindingUtil.bind<FragmentFavoriteBinding>(favoriteRoot)?.apply {
      lifecycleOwner = this@FavoriteFragment.viewLifecycleOwner
      viewModel = this@FavoriteFragment.viewModel
      binding = this
    }
    initKeys()
    //requestAds()
    setupListeners()
  }

  private fun initKeys() {
    longTerm = Pair(getString(R.string.fragment_favorites_long_term), "long_term")
    mediumTerm = Pair(getString(R.string.fragment_favorites_medium_term), "medium_term")
    shortTerm = Pair(getString(R.string.fragment_favorites_short_term), "short_term")
    artistKey = getString(R.string.fragment_favorites_artists)
    trackKey = getString(R.string.fragment_favorites_tracks)
  }

  private fun requestAds() {
    AdColony.configure(
      requireActivity(),
      BuildConfig.ADCOLONY_APP_ID,
      BuildConfig.ADCOLONY_FULL_SCREEN_AD_ZONE_ID
    )
    val listener: AdColonyInterstitialListener = object : AdColonyInterstitialListener() {
      override fun onRequestFilled(ad: AdColonyInterstitial) {
        ad.show()
      }
    }
    AdColony.requestInterstitial(BuildConfig.ADCOLONY_FULL_SCREEN_AD_ZONE_ID, listener)
  }

  private fun setupListeners() {
    binding.type.setOnClickListener {
      it.context.let { context ->
        MaterialDialog(context).show {
          listItems(items = listOf(artistKey, trackKey)) { _, _, text ->
            binding.type.setText(text)
          }
        }
      }
    }

    binding.timeRange.setOnClickListener {
      it.context.let { context ->
        MaterialDialog(context).show {
          listItems(items = listOf(longTerm.first, mediumTerm.first, shortTerm.first)) { _, _, text ->
            binding.timeRange.setText(text)
          }
        }
      }
    }

    binding.show.setOnClickListener {
      it.hideKeyboard()

      val timeRange = when (binding.timeRange.text.toString()) {
        mediumTerm.first -> mediumTerm.second
        shortTerm.first -> shortTerm.second
        else -> longTerm.second
      }

      when (binding.type.text.toString()) {
        artistKey -> openTopArtistsFragment(timeRange)
        trackKey -> openTopTracksFragment(timeRange)
        else -> showError(resources.getString(R.string.fragment_favorites_type_empty_error))
      }
    }
  }

  private fun openTopArtistsFragment(timeRange: String) {
    val action = FavoriteFragmentDirections.actionFavoriteFragmentToTopArtistsFragment(timeRange)
    findNavController().navigate(action)
  }

  private fun openTopTracksFragment(timeRange: String) {
    val action = FavoriteFragmentDirections.actionFavoriteFragmentToTopTracksFragment(timeRange)
    findNavController().navigate(action)
  }
}
