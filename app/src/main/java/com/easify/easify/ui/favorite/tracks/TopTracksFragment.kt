package com.easify.easify.ui.favorite.tracks

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
import com.easify.easify.databinding.FragmentTopTracksBinding
import com.easify.easify.model.Track
import com.easify.easify.ui.base.BaseFragment
import com.easify.easify.ui.favorite.tracks.adapter.TopTracksAdapter
import com.easify.easify.ui.favorite.tracks.data.TopTracksDataSource
import com.easify.easify.util.EventObserver
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

  private val args: TopTracksFragmentArgs by navArgs()

  private lateinit var topTracksAdapter: TopTracksAdapter

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    DataBindingUtil.bind<FragmentTopTracksBinding>(topTracksRoot)?.apply {
      lifecycleOwner = this@TopTracksFragment.viewLifecycleOwner
      viewModel = this@TopTracksFragment.viewModel
      binding = this
    }
    setupObservers()
    setupTopArtistsAdapter()
  }

  private fun setupObservers() {
    viewModel.event.observe(viewLifecycleOwner, EventObserver { event ->
      when (event) {
        is TopTracksViewEvent.ShowError -> showError(event.message)
      }
    })

    buildPagedListLiveData().observe(viewLifecycleOwner, { list ->
      topTracksAdapter.submitList(list)
    })
  }

  private fun setupTopArtistsAdapter() {
    topTracksAdapter = TopTracksAdapter(viewModel)
    binding.topTracksRecyclerView.adapter = topTracksAdapter
  }

  private fun buildPagedListLiveData(): LiveData<PagedList<Track>> {
    return LivePagedListBuilder(
      object : DataSource.Factory<String, Track>() {
        override fun create(): DataSource<String, Track> {
          return TopTracksDataSource(args.timeRange, viewModel)
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
