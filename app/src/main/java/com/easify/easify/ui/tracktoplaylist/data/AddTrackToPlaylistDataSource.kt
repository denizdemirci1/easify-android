package com.easify.easify.ui.tracktoplaylist.data

import androidx.paging.PageKeyedDataSource
import com.easify.easify.model.Playlist
import com.easify.easify.model.PlaylistResponse
import com.easify.easify.ui.tracktoplaylist.AddTrackToPlaylistViewModel
import javax.inject.Inject

/**
 * @author: deniz.demirci
 * @date: 2.11.2020
 */

class AddTrackToPlaylistDataSource @Inject constructor(
  private val viewModel: AddTrackToPlaylistViewModel
) : PageKeyedDataSource<String, Playlist>() {

  private var callCount = 0
  private val userId: String? = viewModel.getUserId()

  override fun loadInitial(
    params: LoadInitialParams<String>,
    callback: LoadInitialCallback<String, Playlist>
  ) {
    viewModel.getPlaylists(0) { data: PlaylistResponse ->
      val editablePlaylists = data.items.filter { it.owner.id == userId }
      callback.onResult(editablePlaylists, null, data.next)
      callCount++
    }
  }

  override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, Playlist>) {}

  override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, Playlist>) {
    viewModel.getPlaylists(30 * callCount) { data: PlaylistResponse ->
      callCount++
      val editablePlaylists = data.items.filter { it.owner.id == userId }
      callback.onResult(editablePlaylists, data.next)
    }
  }
}
