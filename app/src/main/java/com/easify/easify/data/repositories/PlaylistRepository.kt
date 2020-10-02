package com.easify.easify.data.repositories

import com.easify.easify.data.remote.datasource.PlaylistDataSource
import com.easify.easify.model.PlaylistResponse
import com.easify.easify.model.Result

/**
 * @author: deniz.demirci
 * @date: 9/29/2020
 */

interface PlaylistRepository {
  suspend fun fetchPlaylists(offset: Int): Result<PlaylistResponse>

}

class PlaylistRepositoryImpl(
  private val playlistDataSource: PlaylistDataSource
) : PlaylistRepository {

  override suspend fun fetchPlaylists(offset: Int): Result<PlaylistResponse> {
    return playlistDataSource.fetchPlaylists(offset)
  }
}
