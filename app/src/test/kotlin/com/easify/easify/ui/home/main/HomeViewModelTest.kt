package com.easify.easify.ui.home.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.easify.easify.data.repositories.ArtistRepository
import com.easify.easify.data.repositories.FirebaseRepository
import com.easify.easify.data.repositories.TrackRepository
import com.easify.easify.model.Result
import com.easify.easify.model.response.ArtistsResponse
import com.easify.easify.model.response.TracksResponse
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
class HomeViewModelTest {

  @get:Rule
  var instantExecutorRule = InstantTaskExecutorRule()

  @get:Rule
  var mainCoroutineRule = MainCoroutineRule()

  // Subject under test
  private lateinit var homeViewModel: HomeViewModel

  @MockK
  lateinit var savedStateHandle: SavedStateHandle
  @MockK
  lateinit var firebaseRepository: FirebaseRepository
  @MockK
  lateinit var trackRepository: TrackRepository
  @MockK
  lateinit var artistRepository: ArtistRepository
  @MockK
  lateinit var userManager: UserManager
  @MockK
  lateinit var eventObserver: EventObserver<HomeViewEvent>

  @Before
  fun setUp() {
    MockKAnnotations.init(this, relaxed = true)
    homeViewModel = HomeViewModel(
      savedStateHandle = savedStateHandle,
      firebaseRepository = firebaseRepository,
      trackRepository = trackRepository,
      artistRepository = artistRepository,
      userManager = userManager
    )
  }

  @Test
  fun whenInitialized_ShouldCallForItemsFromFirebaseDatabase() {
    verify { homeViewModel.setFirebaseItemsReceivedListener() }
  }

  @Test
  fun requests_WhenError_ShouldFireErrorEvent() =
    mainCoroutineRule.runBlockingTest {
      val exception = Exception()
      val res = Result.Error(exception)

      coEvery { trackRepository.getTracks(any()) } returns res
      coEvery { artistRepository.getArtists(any()) } returns res

      homeViewModel.getTracks("")

      Truth.assertThat(homeViewModel.event.getOrAwaitValue().getContentIfNotHandled())
        .isInstanceOf(HomeViewEvent.ShowError::class.java)

      homeViewModel.getArtists("")

      Truth.assertThat(homeViewModel.event.getOrAwaitValue().getContentIfNotHandled())
        .isInstanceOf(HomeViewEvent.ShowError::class.java)
    }

  @Test
  fun getTracks_WhenSuccess_ShouldFireFeaturedTracksReceivedEvent() =
    mainCoroutineRule.runBlockingTest {
      val res = Result.Success(TracksResponse(listOf()))

      coEvery { trackRepository.getTracks(any()) } returns res

      homeViewModel.getTracks("")

      Truth.assertThat(homeViewModel.event.getOrAwaitValue().getContentIfNotHandled())
        .isInstanceOf(HomeViewEvent.OnFeaturedTracksReceived::class.java)
    }

  @Test
  fun getArtists_WhenSuccess_ShouldFireFeaturedArtistsReceivedEvent() =
    mainCoroutineRule.runBlockingTest {
      val res = Result.Success(ArtistsResponse(listOf()))

      coEvery { artistRepository.getArtists(any()) } returns res

      homeViewModel.getArtists("")

      Truth.assertThat(homeViewModel.event.getOrAwaitValue().getContentIfNotHandled())
        .isInstanceOf(HomeViewEvent.OnFeaturedArtistsReceived::class.java)
    }

  @Test
  fun authError_ShouldFireAuthenticationEvent() {
    homeViewModel.onAuthError()

    Truth.assertThat(homeViewModel.event.getOrAwaitValue().getContentIfNotHandled())
      .isInstanceOf(HomeViewEvent.Authenticate::class.java)
  }

  @Test
  fun onItemClick_ShouldFireOnItemClickEvent() {
    homeViewModel.onItemClick(mockk(), 0)

    Truth.assertThat(homeViewModel.event.getOrAwaitValue().getContentIfNotHandled())
      .isInstanceOf(HomeViewEvent.OnItemClicked::class.java)
  }

  @After
  fun tearDown() {
    homeViewModel.event.removeObserver(eventObserver)
  }
}
