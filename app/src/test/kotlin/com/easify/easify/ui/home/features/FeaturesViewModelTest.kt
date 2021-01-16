package com.easify.easify.ui.home.features

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.easify.easify.data.repositories.TrackRepository
import com.easify.easify.model.FeaturesResponse
import com.easify.easify.model.Result
import com.easify.easify.util.EventObserver
import com.easify.easify.util.MainCoroutineRule
import com.easify.easify.util.getOrAwaitValue
import com.easify.easify.util.manager.UserManager
import com.google.common.truth.Truth
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
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
class FeaturesViewModelTest {

  @get:Rule
  var instantExecutorRule = InstantTaskExecutorRule()

  @get:Rule
  var mainCoroutineRule = MainCoroutineRule()

  // Subject under test
  private lateinit var featuresViewModel: FeaturesViewModel

  @MockK
  lateinit var savedStateHandle: SavedStateHandle
  @MockK
  lateinit var trackRepository: TrackRepository
  @MockK
  lateinit var userManager: UserManager
  @MockK
  lateinit var eventObserver: EventObserver<FeaturesViewEvent>

  @Before
  fun setUp() {
    MockKAnnotations.init(this, relaxed = true)
    featuresViewModel = FeaturesViewModel(
      savedStateHandle = savedStateHandle,
      trackRepository = trackRepository,
      userManager = userManager
    )
  }

  @Test
  fun getAudioFeatures_WhenError_ShouldFireErrorEvent() =
    mainCoroutineRule.runBlockingTest {
      val exception = Exception()
      val res = Result.Error(exception)

      coEvery { trackRepository.fetchAudioFeatures(any()) } returns res

      featuresViewModel.getAudioFeatures("")

      Truth.assertThat(featuresViewModel.event.getOrAwaitValue().getContentIfNotHandled())
        .isInstanceOf(FeaturesViewEvent.ShowError::class.java)
    }

  @Test
  fun getAudioFeatures_WhenSuccess_ShouldFireOnFeaturesReceivedEvent() =
    mainCoroutineRule.runBlockingTest {
      val res = Result.Success(mockk<FeaturesResponse>())

      coEvery { trackRepository.fetchAudioFeatures(any()) } returns res

      featuresViewModel.getAudioFeatures("")

      Truth.assertThat(featuresViewModel.event.getOrAwaitValue().getContentIfNotHandled())
        .isInstanceOf(FeaturesViewEvent.OnFeaturesReceived::class.java)
    }

  @Test
  fun authError_ShouldFireAuthenticationEvent() {
    featuresViewModel.onAuthError()

    Truth.assertThat(featuresViewModel.event.getOrAwaitValue().getContentIfNotHandled())
      .isInstanceOf(FeaturesViewEvent.Authenticate::class.java)
  }

  @After
  fun tearDown() {
    featuresViewModel.event.removeObserver(eventObserver)
  }
}
