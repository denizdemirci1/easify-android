package com.easify.easify.ui.home.recommendations

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.easify.easify.data.repositories.BrowseRepository
import com.easify.easify.data.repositories.PlaylistRepository
import com.easify.easify.data.repositories.TrackRepository
import com.easify.easify.model.FeaturesResponse
import com.easify.easify.model.Playlist
import com.easify.easify.model.RecommendationsResponse
import com.easify.easify.model.Result
import com.easify.easify.model.util.EasifyItem
import com.easify.easify.model.util.EasifyItemType
import com.easify.easify.ui.home.features.FeaturesViewEvent
import com.easify.easify.ui.home.features.FeaturesViewModel
import com.easify.easify.ui.home.main.HomeViewEvent
import com.easify.easify.ui.home.recommendations.MockDataProvider.provideFeatureResponse
import com.easify.easify.ui.home.recommendations.MockDataProvider.provideRecommendationsResponse
import com.easify.easify.util.EventObserver
import com.easify.easify.util.MainCoroutineRule
import com.easify.easify.util.getOrAwaitValue
import com.easify.easify.util.manager.UserManager
import com.google.common.truth.Truth
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * @author: deniz.demirci
 * @date: 11.01.2021
 */

@ExperimentalCoroutinesApi
class RecommendationsViewModelTest {

  @get:Rule
  var instantExecutorRule = InstantTaskExecutorRule()

  @get:Rule
  var mainCoroutineRule = MainCoroutineRule()

  // Subject under test
  private lateinit var recommendationsViewModel: RecommendationsViewModel

  @MockK
  lateinit var savedStateHandle: SavedStateHandle
  @MockK
  lateinit var browseRepository: BrowseRepository
  @MockK
  lateinit var playlistRepository: PlaylistRepository
  @MockK
  lateinit var userManager: UserManager
  @MockK
  lateinit var eventObserver: EventObserver<RecommendationsViewEvent>

  @Before
  fun setUp() {
    MockKAnnotations.init(this, relaxed = true)
    recommendationsViewModel = RecommendationsViewModel(
      savedStateHandle = savedStateHandle,
      browseRepository = browseRepository,
      playlistRepository = playlistRepository,
      userManager = userManager
    )
  }

  @Test
  fun requests_WhenError_ShouldFireErrorEvent() =
    mainCoroutineRule.runBlockingTest {
      val exception = Exception()
      val res = Result.Error(exception)

      coEvery { browseRepository.fetchRecommendations(any()) } returns res
      coEvery { playlistRepository.fetchPlaylists(any()) } returns res
      coEvery { playlistRepository.createPlaylist(any(), any()) } returns res
      coEvery { playlistRepository.addTrackToPlaylist(any(), any()) } returns res

      recommendationsViewModel.getRecommendations(provideFeatureResponse())
      Truth.assertThat(recommendationsViewModel.event.getOrAwaitValue().getContentIfNotHandled())
        .isInstanceOf(RecommendationsViewEvent.ShowError::class.java)

      recommendationsViewModel.createPlaylist("", "")
      Truth.assertThat(recommendationsViewModel.event.getOrAwaitValue().getContentIfNotHandled())
        .isInstanceOf(RecommendationsViewEvent.ShowError::class.java)

      recommendationsViewModel.findPlaylist()
      Truth.assertThat(recommendationsViewModel.event.getOrAwaitValue().getContentIfNotHandled())
        .isInstanceOf(RecommendationsViewEvent.ShowError::class.java)

      recommendationsViewModel.addTracksToThePlaylist("")
      Truth.assertThat(recommendationsViewModel.event.getOrAwaitValue().getContentIfNotHandled())
        .isInstanceOf(RecommendationsViewEvent.ShowError::class.java)
    }


  @Test
  fun getRecommendations_WhenSuccess_ShouldFireOnRecommendationsReceivedEvent() =
    mainCoroutineRule.runBlockingTest {
      val res = Result.Success(provideRecommendationsResponse())

      coEvery { browseRepository.fetchRecommendations(any()) } returns res

      recommendationsViewModel.getRecommendations(provideFeatureResponse())

      Truth.assertThat(recommendationsViewModel.event.getOrAwaitValue().getContentIfNotHandled())
        .isInstanceOf(RecommendationsViewEvent.OnRecommendationsReceived::class.java)

      Truth.assertThat(recommendationsViewModel.urisOfTracks).isNotEmpty()
    }

  @Test
  fun createPlaylist_WhenSuccess_ShouldSetCreatedPlaylistName() =
    mainCoroutineRule.runBlockingTest {
      val res = Result.Success(mockk<Playlist>())

      coEvery { playlistRepository.createPlaylist(any(), any()) } returns res

      recommendationsViewModel.createPlaylist("name", "description")

      Truth.assertThat(recommendationsViewModel.playlistNameToCreate).isEqualTo("name")
      verify { recommendationsViewModel.findPlaylist() }
    }


  @Test
  fun authError_ShouldFireAuthenticationEvent() {
    recommendationsViewModel.onAuthError()

    Truth.assertThat(recommendationsViewModel.event.getOrAwaitValue().getContentIfNotHandled())
      .isInstanceOf(RecommendationsViewEvent.Authenticate::class.java)
  }

  @Test
  fun onAddIconClick_WhenEasifyTrackIsNotNull_shouldSendOnAddIconClickEvent() =
    mainCoroutineRule.runBlockingTest {

      val easifyItemWithTrack = EasifyItem(type = EasifyItemType.TRACK, track = mockk())
      val easifyItemWithNullTrack = EasifyItem(type = EasifyItemType.TRACK)

      recommendationsViewModel.onAddIconClick(easifyItemWithTrack)
      Truth.assertThat(recommendationsViewModel.event.getOrAwaitValue().getContentIfNotHandled())
        .isInstanceOf(RecommendationsViewEvent.AddIconClicked::class.java)

      recommendationsViewModel.onAddIconClick(easifyItemWithNullTrack)
      Truth.assertThat(recommendationsViewModel.event.getOrAwaitValue().getContentIfNotHandled())
        .isNotInstanceOf(RecommendationsViewEvent.AddIconClicked::class.java)
    }

  @After
  fun tearDown() {
    recommendationsViewModel.event.removeObserver(eventObserver)
  }
}
