package com.easify.easify.ui.favorite

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.easify.easify.R
import com.easify.easify.databinding.FragmentFavoriteBinding
import com.easify.easify.ui.base.BaseFragment
import com.easify.easify.ui.extensions.hideKeyboard
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_favorite.*

/**
 * @author: deniz.demirci
 * @date: 9/5/2020
 */

private const val KEY_ARTISTS = "artists"
private const val KEY_TRACKS = "tracks"

@AndroidEntryPoint
class FavoriteFragment : BaseFragment(R.layout.fragment_favorite) {

  private val viewModel by viewModels<FavoriteViewModel>()

  private lateinit var binding: FragmentFavoriteBinding

  private val longTerm = Pair("Several Years", "long_term")
  private val mediumTerm = Pair("Last 6 Months", "medium_term")
  private val shortTerm = Pair("Last 4 Weeks", "short_term")

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    DataBindingUtil.bind<FragmentFavoriteBinding>(favoriteRoot)?.apply {
      lifecycleOwner = this@FavoriteFragment.viewLifecycleOwner
      viewModel = this@FavoriteFragment.viewModel
      binding = this
    }
    initAds()
    setupListeners()
  }

  private fun initAds() {
    MobileAds.initialize(activity) {}
    MobileAds.setRequestConfiguration(
      RequestConfiguration.Builder()
        .setTestDeviceIds(listOf("0D0FF4FD4C0328983D7FFC930B2555E3"))
        .build()
    )
    val adRequest = AdRequest.Builder().build()
    binding.adView.loadAd(adRequest)
  }

  private fun setupListeners() {
    binding.type.setOnClickListener {
      it.context.let { context ->
        MaterialDialog(context).show {
          listItems(items = listOf(KEY_ARTISTS, KEY_TRACKS)) { _, _, text ->
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
        KEY_ARTISTS -> openTopArtistsFragment(timeRange)
        KEY_TRACKS -> openTopTracksFragment(timeRange)
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

  private fun showError(message: String) {
    MaterialDialog(requireContext()).show {
      title(R.string.dialog_error_title)
      message(text = message)
      positiveButton(R.string.dialog_ok)
    }
  }

  override fun onPause() {
    binding.adView.pause()
    super.onPause()
  }

  override fun onResume() {
    super.onResume()
    binding.adView.resume()
  }

  override fun onDestroyView() {
    binding.adView.destroy()
    super.onDestroyView()
  }
}
