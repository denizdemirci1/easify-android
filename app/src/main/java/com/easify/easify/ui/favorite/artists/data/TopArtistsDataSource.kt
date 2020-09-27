package com.easify.easify.ui.favorite.artists.data

import androidx.paging.PageKeyedDataSource
import com.easify.easify.model.Artist
import com.easify.easify.model.TopArtistResponse
import com.easify.easify.ui.favorite.artists.TopArtistsViewModel
import javax.inject.Inject

/**
 * @author: deniz.demirci
 * @date: 9/26/2020
 */

class TopArtistsDataSource @Inject constructor(
  private val timeRange: String,
  private var userRequestedLimit: Int,
  private val viewModel: TopArtistsViewModel
) : PageKeyedDataSource<String, Artist>() {

  override fun loadInitial(
    params: LoadInitialParams<String>,
    callback: LoadInitialCallback<String, Artist>
  ) {
    val limit = if (userRequestedLimit < params.requestedLoadSize)
      userRequestedLimit else
      params.requestedLoadSize

    viewModel.getTopArtists(timeRange, limit, 0) { data: TopArtistResponse ->
      callback.onResult(
        data.items,
        null,
        data.next
      )
    }
    userRequestedLimit -= params.requestedLoadSize
  }

  override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, Artist>) {}

  override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, Artist>) {
    val limit = if (userRequestedLimit < params.requestedLoadSize)
      userRequestedLimit else
      params.requestedLoadSize
    viewModel.getTopArtists(timeRange, limit, params.requestedLoadSize) { data: TopArtistResponse ->
      callback.onResult(
        if (userRequestedLimit <= 0) listOf() else data.items,
        data.next
      )
    }
    userRequestedLimit -= params.requestedLoadSize
  }
}
