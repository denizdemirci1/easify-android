package com.easify.easify.data.remote.datasource

import com.easify.easify.data.service.SpotifyService
import com.easify.easify.model.*

/**
 * @author: deniz.demirci
 * @date: 9/29/2020
 */

interface PlaylistDataSource {
  suspend fun fetchPlaylists(offset: Int): Result<PlaylistResponse>

  suspend fun fetchPlaylistTracks(playlistId: String, offset: Int): Result<PlaylistTracksResponse>

  suspend fun removeTracksFromPlaylist(playlistId: String, removeTracksObject: RemoveTrackObject)

  suspend fun createPlaylist(userId: String, body: CreatePlaylistBody): Result<Playlist>
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

  override suspend fun createPlaylist(userId: String, body: CreatePlaylistBody): Result<Playlist> {
    return try {
      val playlistObject = service.createPlaylist(userId, body)
      Result.Success(playlistObject)
    } catch (e: Exception) {
      Result.Error(e)
    }
  }
}
