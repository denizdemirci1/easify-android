package com.easify.easify.di

import com.easify.easify.data.remote.datasource.UserDataSource
import com.easify.easify.data.repositories.UserRepository
import com.easify.easify.data.repositories.UserRepositoryImpl
import com.easify.easify.util.storage.Storage
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
    storage: Storage
  ): UserRepository {
    return UserRepositoryImpl(userDataSource, storage)
  }

}