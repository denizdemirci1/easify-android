package com.easify.easify.ui.favorite.artists.data

import androidx.paging.PageKeyedDataSource
import com.easify.easify.model.TopArtistResponse
import com.easify.easify.model.util.EasifyItem
import com.easify.easify.ui.extensions.toEasifyItemList
import com.easify.easify.ui.favorite.artists.TopArtistsViewModel
import javax.inject.Inject

/**
 * @author: deniz.demirci
 * @date: 9/26/2020
 */

class TopArtistsDataSource @Inject constructor(
  private val timeRange: String,
  private val viewModel: TopArtistsViewModel
) : PageKeyedDataSource<String, EasifyItem>() {

  private var callCount = 0

  override fun loadInitial(
    params: LoadInitialParams<String>,
    callback: LoadInitialCallback<String, EasifyItem>
  ) {
    viewModel.getTopArtists(timeRange, 0) { data: TopArtistResponse ->
      callback.onResult(data.items.toEasifyItemList(), null, data.next)
      callCount++
    }
  }

  override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, EasifyItem>) {}

  override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, EasifyItem>) {
    viewModel.getTopArtists(timeRange, 20 * callCount) { data: TopArtistResponse ->
      callCount++
      callback.onResult(data.items.toEasifyItemList(), data.next)
    }
  }
}
