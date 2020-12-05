package com.easify.easify.data.remote.datasource

import com.easify.easify.data.service.SpotifyService
import javax.inject.Inject

/**
 * @author: deniz.demirci
 * @date: 9/4/2020
 */

interface UserDataSource {
  suspend fun fetchUser()
}

class UserDataSourceImpl @Inject constructor(
  val spotifyService: SpotifyService
): UserDataSource {

  override suspend fun fetchUser() {
    TODO("Not yet implemented")
  }
}