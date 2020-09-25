package com.easify.easify.di

import com.easify.easify.data.remote.datasource.*
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
  fun provideUserDataSource(spotifyService: SpotifyService): UserDataSource {
    return UserDataSourceImpl(spotifyService)
  }

  @Provides
  @ActivityRetainedScoped
  fun providePlayerDataSource(spotifyService: SpotifyService): PlayerDataSource {
    return PlayerDataSourceImpl(spotifyService)
  }

  @Provides
  @ActivityRetainedScoped
  fun providePersonalizationDataSource(spotifyService: SpotifyService): PersonalizationDataSource {
    return PersonalizationDataSourceImpl(spotifyService)
  }
}
