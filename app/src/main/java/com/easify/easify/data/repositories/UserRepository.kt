package com.easify.easify.data.repositories

import androidx.annotation.VisibleForTesting
import com.easify.easify.data.remote.datasource.UserDataSource
import com.easify.easify.model.Result
import com.easify.easify.model.User
import com.easify.easify.util.manager.UserManager
import javax.inject.Inject

/**
 * @author: deniz.demirci
 * @date: 9/4/2020
 */

interface UserRepository {

  suspend fun fetchUser(): Result<User>?

  fun getToken(): String?
}

class UserRepositoryImpl @Inject constructor(
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal val userDataSource: UserDataSource,
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal val userManager: UserManager
) : UserRepository {

  override suspend fun fetchUser(): Result<User>? {
    return userDataSource.fetchUser()
  }

  override fun getToken() = userManager.token
}
