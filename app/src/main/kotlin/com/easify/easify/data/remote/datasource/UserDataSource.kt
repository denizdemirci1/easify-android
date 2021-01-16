package com.easify.easify.data.remote.datasource

import com.easify.easify.data.service.SpotifyService
import com.easify.easify.model.Result
import com.easify.easify.model.User
import javax.inject.Inject

/**
 * @author: deniz.demirci
 * @date: 9/4/2020
 */

interface UserDataSource {

  suspend fun fetchUser(): Result<User>?
}

class UserDataSourceImpl @Inject constructor(
    private val spotifyService: SpotifyService
) : UserDataSource {

  override suspend fun fetchUser(): Result<User>? {
    return try {
      val user = spotifyService.fetchUser()
      Result.Success(user)
    } catch (e: Exception) {
      Result.Error(e)
    }
  }
}
