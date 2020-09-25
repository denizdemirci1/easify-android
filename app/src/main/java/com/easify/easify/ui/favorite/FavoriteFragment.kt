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
    setupListeners()
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

      val amount = when {
        binding.limit.text.toString() == "0" -> {
          showError(getString(R.string.fragment_favorites_amount_zero_error))
          return@setOnClickListener
        }
        binding.limit.text.toString().isNotEmpty() -> Integer.parseInt(binding.limit.text.toString())
        else -> 50
      }

      when (binding.type.text.toString()) {
        KEY_ARTISTS -> openTopArtistsFragment(timeRange, amount)
        KEY_TRACKS -> openTopTracksFragment(timeRange, amount)
        else -> showError(resources.getString(R.string.fragment_favorites_type_empty_error))
      }
    }
  }

  private fun openTopArtistsFragment(timeRange: String, amount: Int) {
    val action = FavoriteFragmentDirections.actionFavoriteFragmentToTopArtistsFragment(
      timeRange, amount
    )
    findNavController().navigate(action)
  }

  private fun openTopTracksFragment(timeRange: String, amount: Int) {
    val action = FavoriteFragmentDirections.actionFavoriteFragmentToTopTracksFragment(
      timeRange, amount
    )
    findNavController().navigate(action)
  }

  private fun showError(message: String) {
    MaterialDialog(requireContext()).show {
      title(R.string.dialog_error_title)
      message(text = message)
      positiveButton(R.string.dialog_ok)
    }
  }
}
