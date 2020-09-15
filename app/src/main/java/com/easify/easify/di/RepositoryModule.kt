package com.easify.easify.di

import com.easify.easify.data.remote.datasource.PlayerDataSource
import com.easify.easify.data.remote.datasource.UserDataSource
import com.easify.easify.data.repositories.PlayerRepository
import com.easify.easify.data.repositories.PlayerRepositoryImpl
import com.easify.easify.data.repositories.UserRepository
import com.easify.easify.data.repositories.UserRepositoryImpl
import com.easify.easify.util.manager.UserManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

/**
 * @author: deniz.demirci
 * @date: 9/4/2020
 */

@Module
@InstallIn(ActivityRetainedComponent::class)
object RepositoryModule {

  @Provides
  @ActivityRetainedScoped
  fun provideUserRepository(
      userDataSource: UserDataSource,
      userManager: UserManager
  ): UserRepository {
    return UserRepositoryImpl(userDataSource, userManager)
  }

  @Provides
  @ActivityRetainedScoped
  fun providePlayerRepository(
    playerDataSource: PlayerDataSource
  ): PlayerRepository {
    return PlayerRepositoryImpl(playerDataSource)
  }
}
