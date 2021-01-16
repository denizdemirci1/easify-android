package com.easify.easify.data.remote.datasource

import com.easify.easify.data.service.SpotifyService
import com.easify.easify.model.Result
import com.easify.easify.model.SearchResponse
import com.easify.easify.model.SearchType

/**
 * @author: deniz.demirci
 * @date: 28.11.2020
 */

interface SearchDataSource {

  suspend fun search(type: SearchType, query: String): Result<SearchResponse>
}

class SearchDataSourceImpl(
  private val service: SpotifyService
) : SearchDataSource {

  override suspend fun search(type: SearchType, query: String): Result<SearchResponse> {
    return try {
      val searchResult = service.search(type.value, query)
      Result.Success(searchResult)
    } catch (e: Exception) {
      Result.Error(e)
    }
  }
}
