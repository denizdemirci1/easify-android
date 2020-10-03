package com.easify.easify.data.remote.datasource

import com.easify.easify.data.service.SpotifyService
import com.easify.easify.model.PlaylistResponse
import com.easify.easify.model.PlaylistTracksResponse
import com.easify.easify.model.RemoveTrackObject
import com.easify.easify.model.Result

/**
 * @author: deniz.demirci
 * @date: 9/29/2020
 */

interface PlaylistDataSource {
  suspend fun fetchPlaylists(offset: Int): Result<PlaylistResponse>

  suspend fun fetchPlaylistTracks(playlistId: String, offset: Int): Result<PlaylistTracksResponse>

  suspend fun removeTracksFromPlaylist(playlistId: String, removeTracksObject: RemoveTrackObject)
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

  override suspend fun fetchPlaylistTracks(
    playlistId: String,
    offset: Int
  ): Result<PlaylistTracksResponse> {
    return try {
      val playlistTracksObject = service.fetchPlaylistTracks(
        playlistId = playlistId,
        offset = offset
      )
      Result.Success(playlistTracksObject)
    } catch (e: Exception) {
      Result.Error(e)
    }
  }

  override suspend fun removeTracksFromPlaylist(
    playlistId: String,
    removeTracksObject: RemoveTrackObject
  ) {
    service.removeTracksFromPlaylist(playlistId, removeTracksObject)
  }
}
