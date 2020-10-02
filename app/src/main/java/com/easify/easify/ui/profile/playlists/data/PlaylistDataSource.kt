package com.easify.easify.ui.profile.playlists.data

import androidx.paging.PageKeyedDataSource
import com.easify.easify.model.Playlist
import com.easify.easify.model.PlaylistResponse
import com.easify.easify.ui.profile.playlists.PlaylistViewModel
import javax.inject.Inject

/**
 * @author: deniz.demirci
 * @date: 9/30/2020
 */

class PlaylistDataSource @Inject constructor(
  private val viewModel: PlaylistViewModel
) : PageKeyedDataSource<String, Playlist>() {

  private var callCount = 0

  override fun loadInitial(
    params: LoadInitialParams<String>,
    callback: LoadInitialCallback<String, Playlist>
  ) {
    viewModel.getPlaylists(0) { data: PlaylistResponse ->
      callback.onResult(data.items, null, data.next)
      callCount++
    }
  }

  override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, Playlist>) {}

  override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, Playlist>) {
    viewModel.getPlaylists(20 * callCount) { data: PlaylistResponse ->
      callCount++
      callback.onResult(data.items, data.next)
    }
  }
}
