package com.easify.easify.data.repositories

import com.easify.easify.data.remote.datasource.LibraryDataSource
import com.easify.easify.model.Result
import com.easify.easify.model.SavedTrackResponse

/**
 * @author: deniz.demirci
 * @date: 8.11.2020
 */

interface LibraryRepository {

  suspend fun fetchSavedTracks(offset: Int): Result<SavedTrackResponse>

  suspend fun saveTracks(id: String)

  suspend fun checkSavedTracks(id: String): Result<List<Boolean>>

}

class LibraryRepositoryImpl(
  private val libraryDataSource: LibraryDataSource
) : LibraryRepository {

  override suspend fun fetchSavedTracks(offset: Int): Result<SavedTrackResponse> {
    return libraryDataSource.fetchSavedTracks(offset)
  }

  override suspend fun saveTracks(id: String) {
    libraryDataSource.saveTracks(id)
  }

  override suspend fun checkSavedTracks(id: String): Result<List<Boolean>> {
    return libraryDataSource.checkSavedTracks(id)
  }
}
