package com.easify.easify.data.repositories

import androidx.annotation.VisibleForTesting
import com.easify.easify.data.remote.datasource.UserDataSource
import com.easify.easify.util.storage.Storage
import javax.inject.Inject

/**
 * @author: deniz.demirci
 * @date: 9/4/2020
 */

interface UserRepository {
  suspend fun fetchUser()
}

class UserRepositoryImpl @Inject constructor(
  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  internal val userDataSource: UserDataSource,
  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  internal val storage: Storage
): UserRepository {

  override suspend fun fetchUser() {
    TODO("Not yet implemented")
  }
}