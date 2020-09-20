package com.easify.easify.ui.history

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.afollestad.materialdialogs.MaterialDialog
import com.easify.easify.R
import com.easify.easify.databinding.FragmentHistoryBinding
import com.easify.easify.model.History
import com.easify.easify.model.Track
import com.easify.easify.ui.base.BaseFragment
import com.easify.easify.ui.history.data.HistoryDataSource
import com.easify.easify.ui.history.util.HistoryAdapter
import com.easify.easify.util.EventObserver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_history.*

/**
 * @author: deniz.demirci
 * @date: 9/5/2020
 */

@AndroidEntryPoint
class HistoryFragment : BaseFragment(R.layout.fragment_history) {

  private val viewModel by viewModels<HistoryViewModel>()

  private lateinit var binding: FragmentHistoryBinding

  private lateinit var historyAdapter: HistoryAdapter

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    DataBindingUtil.bind<FragmentHistoryBinding>(historyRoot)?.apply {
      lifecycleOwner = this@HistoryFragment.viewLifecycleOwner
      viewModel = this@HistoryFragment.viewModel
      binding = this
    }
    setupObservers()
    setupHistoryAdapter()
  }

  private fun setupObservers() {
    viewModel.event.observe(viewLifecycleOwner, EventObserver { event ->
      when (event) {
        HistoryViewEvent.ShowOpenSpotifyWarning -> showOpenSpotifyWarning()
        is HistoryViewEvent.OnAddClicked -> onAddClicked(event.track)
        is HistoryViewEvent.ShowError -> showError(event.message)
      }
    })

    buildPagedListLiveData().observe(viewLifecycleOwner, { list ->
      historyAdapter.submitList(list)
    })
  }

  private fun setupHistoryAdapter() {
    historyAdapter = HistoryAdapter(viewModel)
    binding.tracksRecyclerView.adapter = historyAdapter
  }

  private fun buildPagedListLiveData(): LiveData<PagedList<History>> {
    return LivePagedListBuilder(
      object : DataSource.Factory<String, History>() {
        override fun create(): DataSource<String, History> {
          return HistoryDataSource(viewModel)
        }
      }, 30).build()
  }

  private fun showError(message: String) {
    MaterialDialog(requireContext()).show {
      title(R.string.dialog_error_title)
      message(text = message)
      positiveButton(R.string.dialog_ok)
    }
  }

  private fun showOpenSpotifyWarning() {
    MaterialDialog(requireContext()).show {
      title(R.string.dialog_error_title)
      message(R.string.dialog_should_open_spotify)
      positiveButton(R.string.dialog_ok)
    }
  }

  private fun onAddClicked(track: Track) {
    //TODO: open playlist page
  }
}
