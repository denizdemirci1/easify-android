package com.easify.easify.di

import com.easify.easify.data.remote.datasource.UserDataSource
import com.easify.easify.data.remote.datasource.UserDataSourceImpl
import com.easify.easify.data.service.SpotifyService
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
object DataSourceModule {

  @Provides
  @ActivityRetainedScoped
  fun provideUserDataSource(spotifyService: SpotifyService) : UserDataSource {
    return UserDataSourceImpl(spotifyService)
  }

}