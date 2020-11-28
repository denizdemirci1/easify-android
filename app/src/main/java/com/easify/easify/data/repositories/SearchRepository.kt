package com.easify.easify.data.repositories

import com.easify.easify.data.remote.datasource.SearchDataSource
import com.easify.easify.model.Result
import com.easify.easify.model.SearchResponse
import com.easify.easify.model.SearchType

/**
 * @author: deniz.demirci
 * @date: 28.11.2020
 */

interface SearchRepository {

  suspend fun search(type: SearchType, query: String): Result<SearchResponse>

}

class SearchRepositoryImpl(
  private val searchDataSource: SearchDataSource
) : SearchRepository {

  override suspend fun search(type: SearchType, query: String): Result<SearchResponse> {
    return searchDataSource.search(type, query)
  }
}
