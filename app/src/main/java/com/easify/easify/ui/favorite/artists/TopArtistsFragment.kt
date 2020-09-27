package com.easify.easify.ui.favorite.artists

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.navArgs
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.afollestad.materialdialogs.MaterialDialog
import com.easify.easify.R
import com.easify.easify.databinding.FragmentTopArtistsBinding
import com.easify.easify.model.Artist
import com.easify.easify.ui.base.BaseFragment
import com.easify.easify.ui.favorite.artists.adapter.TopArtistsAdapter
import com.easify.easify.ui.favorite.artists.data.TopArtistsDataSource
import com.easify.easify.util.EventObserver
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

  private val args: TopArtistsFragmentArgs by navArgs()

  private lateinit var topArtistsAdapter: TopArtistsAdapter

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    DataBindingUtil.bind<FragmentTopArtistsBinding>(topArtistsRoot)?.apply {
      lifecycleOwner = this@TopArtistsFragment.viewLifecycleOwner
      viewModel = this@TopArtistsFragment.viewModel
      binding = this
    }
    setupObservers()
    setupTopArtistsAdapter()
  }

  private fun setupObservers() {
    viewModel.event.observe(viewLifecycleOwner, EventObserver { event ->
      when (event) {
        is TopArtistsViewEvent.ShowError -> showError(event.message)
      }
    })

    buildPagedListLiveData().observe(viewLifecycleOwner, { list ->
      topArtistsAdapter.submitList(list)
    })
  }

  private fun setupTopArtistsAdapter() {
    topArtistsAdapter = TopArtistsAdapter(viewModel)
    binding.topArtistsRecyclerView.adapter = topArtistsAdapter
  }

  private fun buildPagedListLiveData(): LiveData<PagedList<Artist>> {
    return LivePagedListBuilder(
      object : DataSource.Factory<String, Artist>() {
        override fun create(): DataSource<String, Artist> {
          return TopArtistsDataSource(args.timeRange, viewModel)
        }
      }, 20).build()
  }

  private fun showError(message: String) {
    MaterialDialog(requireContext()).show {
      title(R.string.dialog_error_title)
      message(text = message)
      positiveButton(R.string.dialog_ok)
    }
  }
}
