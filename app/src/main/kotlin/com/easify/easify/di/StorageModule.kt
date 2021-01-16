package com.easify.easify.di

import com.easify.easify.util.storage.SharedPreferencesStorage
import com.easify.easify.util.storage.Storage
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

/**
 * @author: deniz.demirci
 * @date: 9/4/2020
 */

@InstallIn(ApplicationComponent::class)
// Tells Dagger this is a Dagger module
@Module
abstract class StorageModule {

  // Makes Dagger provide SharedPreferencesStorage when a Storage type is requested
  @Binds
  abstract fun provideStorage(storage: SharedPreferencesStorage): Storage
}
