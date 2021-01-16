package com.easify.easify.ui.profile.follows.followed.data

import androidx.paging.PageKeyedDataSource
import com.easify.easify.model.FollowedArtistsResponse
import com.easify.easify.model.util.EasifyItem
import com.easify.easify.ui.extensions.toEasifyItemList
import com.easify.easify.ui.profile.follows.followed.FollowedArtistsViewModel
import javax.inject.Inject

/**
 * @author: deniz.demirci
 * @date: 10/4/2020
 */

class FollowedArtistsDataSource @Inject constructor(
  private val followedArtistsViewModel: FollowedArtistsViewModel
) : PageKeyedDataSource<String, EasifyItem>() {

  override fun loadInitial(
    params: LoadInitialParams<String>,
    callback: LoadInitialCallback<String, EasifyItem>
  ) {
    followedArtistsViewModel.getFollowedArtists(null) { data: FollowedArtistsResponse ->
      callback.onResult(
        data.artists.items.toEasifyItemList(),
        null,
        data.artists.cursor?.after
      )
    }
  }

  override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, EasifyItem>) {}

  override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, EasifyItem>) {
    followedArtistsViewModel.getFollowedArtists(params.key) { data: FollowedArtistsResponse ->
      callback.onResult(data.artists.items.toEasifyItemList(), data.artists.cursor?.after)
    }
  }
}
