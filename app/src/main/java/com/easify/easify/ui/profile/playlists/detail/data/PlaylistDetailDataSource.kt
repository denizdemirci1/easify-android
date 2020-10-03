package com.easify.easify.ui.profile.playlists.detail.data

import androidx.paging.PageKeyedDataSource
import com.easify.easify.model.PlaylistTrack
import com.easify.easify.ui.profile.playlists.detail.PlaylistDetailViewModel
import javax.inject.Inject

/**
 * @author: deniz.demirci
 * @date: 10/3/2020
 */

private const val PAGE_SIZE = 50

class PlaylistDetailDataSource @Inject constructor(
  private val playlistId: String,
  private val viewModel: PlaylistDetailViewModel
) : PageKeyedDataSource<String, PlaylistTrack>() {

  private var callCount = 0

  override fun loadInitial(
    params: LoadInitialParams<String>,
    callback: LoadInitialCallback<String, PlaylistTrack>
  ) {
    viewModel.getPlaylistTracks(playlistId, 0) { data ->
      callback.onResult(data.items, null, data.next)
      callCount++
    }
  }

  override fun loadBefore(
    params: LoadParams<String>,
    callback: LoadCallback<String, PlaylistTrack>
  ) {}

  override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, PlaylistTrack>) {
    viewModel.getPlaylistTracks(playlistId, PAGE_SIZE * callCount) { data ->
      callCount++
      callback.onResult(data.items, data.next)
    }
  }
}
