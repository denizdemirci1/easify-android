package com.easify.easify.data.remote.datasource

import com.easify.easify.data.service.SpotifyService
import com.easify.easify.model.Result
import com.easify.easify.model.SavedTrackResponse

/**
 * @author: deniz.demirci
 * @date: 8.11.2020
 */

interface LibraryDataSource {

  suspend fun fetchSavedTracks(offset: Int): Result<SavedTrackResponse>

  suspend fun saveTracks(id: String)

  suspend fun checkSavedTracks(id: String): Result<List<Boolean>>
}

class LibraryDataSourceImpl(
  private val service: SpotifyService
) : LibraryDataSource {

  override suspend fun fetchSavedTracks(offset: Int): Result<SavedTrackResponse> {
    return try {
      val savedTracks = service.fetchSavedTracks(offset = offset)
      Result.Success(savedTracks)
    } catch (e: Exception) {
      Result.Error(e)
    }
  }

  override suspend fun saveTracks(id: String) {
    service.saveTracks(id)
  }

  override suspend fun checkSavedTracks(id: String): Result<List<Boolean>> {
    return try {
      val checkTrackResponse = service.checkSavedTracks(id)
      Result.Success(checkTrackResponse)
    } catch (e: Exception) {
      Result.Error(e)
    }
  }
}
