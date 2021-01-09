package com.easify.easify.di

import com.easify.easify.data.remote.datasource.*
import com.easify.easify.data.repositories.*
import com.easify.easify.util.manager.UserManager
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
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

  @Provides
  @ActivityRetainedScoped
  fun providePersonalizationRepository(
    personalizationDataSource: PersonalizationDataSource
  ): PersonalizationRepository {
    return PersonalizationRepositoryImpl(personalizationDataSource)
  }

  @Provides
  @ActivityRetainedScoped
  fun providePlaylistRepository(
    playlistDataSource: PlaylistDataSource
  ): PlaylistRepository {
    return PlaylistRepositoryImpl(playlistDataSource)
  }

  @Provides
  @ActivityRetainedScoped
  fun provideFollowRepository(
    followDataSource: FollowDataSource
  ): FollowRepository {
    return FollowRepositoryImpl(followDataSource)
  }

  @Provides
  @ActivityRetainedScoped
  fun provideLibraryRepository(
    libraryDataSource: LibraryDataSource
  ): LibraryRepository {
    return LibraryRepositoryImpl(libraryDataSource)
  }

  @Provides
  @ActivityRetainedScoped
  fun provideSearchRepository(
    searchDataSource: SearchDataSource
  ): SearchRepository {
    return SearchRepositoryImpl(searchDataSource)
  }

  @Provides
  @ActivityRetainedScoped
  fun provideTrackRepository(
    trackDataSource: TrackDataSource
  ): TrackRepository {
    return TrackRepositoryImpl(trackDataSource)
  }

  @Provides
  @ActivityRetainedScoped
  fun provideArtistRepository(
    artistsDataSource: ArtistsDataSource
  ): ArtistRepository {
    return ArtistRepositoryImpl(artistsDataSource)
  }

  @Provides
  @ActivityRetainedScoped
  fun provideBrowseRepository(
    browseDataSource: BrowseDataSource
  ): BrowseRepository {
    return BrowseRepositoryImpl(browseDataSource)
  }

  @Provides
  @ActivityRetainedScoped
  fun provideFirebaseRepository(): FirebaseRepository {
    val database = Firebase.database
    return FirebaseRepositoryImpl(database)
  }
}
