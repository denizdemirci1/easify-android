package com.easify.easify.data.repositories

import com.easify.easify.data.remote.datasource.PlaylistDataSource
import com.easify.easify.model.*

/**
 * @author: deniz.demirci
 * @date: 9/29/2020
 */

interface PlaylistRepository {
  suspend fun fetchPlaylists(offset: Int): Result<PlaylistResponse>

  suspend fun fetchPlaylistTracks(playlistId: String, offset: Int): Result<PlaylistTracksResponse>

  suspend fun removeTracksFromPlaylist(playlistId: String, removeTracksObject: RemoveTrackObject)

  suspend fun createPlaylist(userId: String, body: CreatePlaylistBody): Result<Playlist>
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

  override suspend fun createPlaylist(userId: String, body: CreatePlaylistBody): Result<Playlist> {
    return playlistDataSource.createPlaylist(userId, body)
  }
}
