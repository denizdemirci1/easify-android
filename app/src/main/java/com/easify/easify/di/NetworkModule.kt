package com.easify.easify.di

import com.easify.easify.data.remote.util.SpotifyInterceptor
import com.easify.easify.data.service.SpotifyService
import com.easify.easify.util.manager.UserManager
import com.easify.easify.util.storage.Storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author: deniz.demirci
 * @date: 9/4/2020
 */

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

  private const val CONNECT_TIMEOUT = 10L
  private const val WRITE_TIMEOUT = 1L
  private const val READ_TIMEOUT = 20L

  @Provides
  @Singleton
  fun provideOkHttpClient(storage: Storage): OkHttpClient {
    return OkHttpClient.Builder().apply {
      connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
      writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
      readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
      retryOnConnectionFailure(true)
      addInterceptor(HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
      })
      addInterceptor(SpotifyInterceptor(UserManager(storage)))
    }.build()
  }

  @Provides
  @Singleton
  fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
      .client(okHttpClient)
      .baseUrl(SpotifyService.BASE_URL)
      .addConverterFactory(GsonConverterFactory.create())
      .build()
  }

  @Provides
  @Singleton
  fun provideSpotifyService(retrofit: Retrofit): SpotifyService {
    return retrofit.create(SpotifyService::class.java)
  }
}
