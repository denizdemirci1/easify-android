package com.easify.easify.ui.profile.playlists.main.data

import androidx.paging.PageKeyedDataSource
import com.easify.easify.model.PlaylistResponse
import com.easify.easify.model.util.EasifyItem
import com.easify.easify.ui.extensions.toEasifyItemList
import com.easify.easify.ui.profile.playlists.main.PlaylistViewModel
import javax.inject.Inject

/**
 * @author: deniz.demirci
 * @date: 9/30/2020
 */

class PlaylistDataSource @Inject constructor(
  private val viewModel: PlaylistViewModel
) : PageKeyedDataSource<String, EasifyItem>() {

  private var callCount = 0

  override fun loadInitial(
    params: LoadInitialParams<String>,
    callback: LoadInitialCallback<String, EasifyItem>
  ) {
    viewModel.getPlaylists(0) { data: PlaylistResponse ->
      callback.onResult(data.items.toEasifyItemList(), null, data.next)
      callCount++
    }
  }

  override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, EasifyItem>) {}

  override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, EasifyItem>) {
    viewModel.getPlaylists(20 * callCount) { data: PlaylistResponse ->
      callCount++
      callback.onResult(data.items.toEasifyItemList(), data.next)
    }
  }
}
