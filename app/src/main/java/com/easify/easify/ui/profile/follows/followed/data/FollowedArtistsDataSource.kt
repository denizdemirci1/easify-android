package com.easify.easify.ui.profile.follows.followed.data

import androidx.paging.PageKeyedDataSource
import com.easify.easify.model.Artist
import com.easify.easify.model.ArtistsResponse
import com.easify.easify.ui.profile.follows.followed.FollowedArtistsViewModel
import javax.inject.Inject

/**
 * @author: deniz.demirci
 * @date: 10/4/2020
 */

class FollowedArtistsDataSource @Inject constructor(
  private val followedArtistsViewModel: FollowedArtistsViewModel
) : PageKeyedDataSource<String, Artist>() {

  override fun loadInitial(
    params: LoadInitialParams<String>,
    callback: LoadInitialCallback<String, Artist>
  ) {
    followedArtistsViewModel.getFollowedArtists(null) { data: ArtistsResponse ->
      callback.onResult(data.artists.items, null, data.artists.cursor?.after)
    }
  }

  override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, Artist>) {}

  override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, Artist>) {
    followedArtistsViewModel.getFollowedArtists(params.key) { data: ArtistsResponse ->
      callback.onResult(data.artists.items, data.artists.cursor?.after)
    }
  }
}
