package com.easify.easify.ui.favorite.tracks.data

import androidx.paging.PageKeyedDataSource
import com.easify.easify.model.TopTrackResponse
import com.easify.easify.model.Track
import com.easify.easify.ui.favorite.tracks.TopTracksViewModel
import javax.inject.Inject

/**
 * @author: deniz.demirci
 * @date: 9/27/2020
 */

class TopTracksDataSource @Inject constructor(
  private val timeRange: String,
  private val viewModel: TopTracksViewModel
) : PageKeyedDataSource<String, Track>() {

  private var callCount = 0

  override fun loadInitial(
    params: LoadInitialParams<String>,
    callback: LoadInitialCallback<String, Track>
  ) {
    viewModel.getTopTracks(timeRange, 0) { data: TopTrackResponse ->
      callback.onResult(data.items, null, data.next)
      callCount++
    }
  }

  override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, Track>) {}

  override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, Track>) {
    viewModel.getTopTracks(timeRange, 20 * callCount) { data: TopTrackResponse ->
      callCount++
      callback.onResult(data.items, data.next)
    }
  }
}
