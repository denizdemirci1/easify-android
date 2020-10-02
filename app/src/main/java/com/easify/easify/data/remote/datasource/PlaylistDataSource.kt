package com.easify.easify.data.remote.datasource

import com.easify.easify.data.service.SpotifyService
import com.easify.easify.model.PlaylistResponse
import com.easify.easify.model.Result

/**
 * @author: deniz.demirci
 * @date: 9/29/2020
 */

interface PlaylistDataSource {
  suspend fun fetchPlaylists(offset: Int): Result<PlaylistResponse>

}

class PlaylistDataSourceImpl(
  private val service: SpotifyService
) : PlaylistDataSource {

  override suspend fun fetchPlaylists(offset: Int): Result<PlaylistResponse> {
    return try {
      val playlistObject = service.fetchPlaylists(offset = offset)
      Result.Success(playlistObject)
    } catch (e: Exception) {
      Result.Error(e)
    }
  }
}
