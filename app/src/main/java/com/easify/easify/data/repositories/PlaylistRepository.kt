package com.easify.easify.data.repositories

import com.easify.easify.data.remote.datasource.PlaylistDataSource
import com.easify.easify.model.PlaylistResponse
import com.easify.easify.model.PlaylistTracksResponse
import com.easify.easify.model.RemoveTrackObject
import com.easify.easify.model.Result

/**
 * @author: deniz.demirci
 * @date: 9/29/2020
 */

interface PlaylistRepository {
  suspend fun fetchPlaylists(offset: Int): Result<PlaylistResponse>

  suspend fun fetchPlaylistTracks(playlistId: String, offset: Int): Result<PlaylistTracksResponse>

  suspend fun removeTracksFromPlaylist(playlistId: String, removeTracksObject: RemoveTrackObject)
}

class PlaylistRepositoryImpl(
  private val playlistDataSource: PlaylistDataSource
) : PlaylistRepository {

  override suspend fun fetchPlaylists(offset: Int): Result<PlaylistResponse> {
    return playlistDataSource.fetchPlaylists(offset)
  }

  override suspend fun fetchPlaylistTracks(
    playlistId: String,
    offset: Int
  ): Result<PlaylistTracksResponse> {
    return playlistDataSource.fetchPlaylistTracks(playlistId, offset)
  }

  override suspend fun removeTracksFromPlaylist(
    playlistId: String,
    removeTracksObject: RemoveTrackObject
  ) {
    playlistDataSource.removeTracksFromPlaylist(playlistId, removeTracksObject)
  }
}
