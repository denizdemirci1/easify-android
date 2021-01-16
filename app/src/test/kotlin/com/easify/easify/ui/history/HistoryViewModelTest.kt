package com.easify.easify.ui.history

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.easify.easify.data.repositories.PlayerRepository
import com.easify.easify.model.HistoryResponse
import com.easify.easify.model.Result
import com.easify.easify.model.util.EasifyItem
import com.easify.easify.model.util.EasifyItemType
import com.easify.easify.util.MainCoroutineRule
import com.easify.easify.util.getOrAwaitValue
import com.easify.easify.util.manager.UserManager
import com.google.common.truth.Truth
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*

/**
 * @author: deniz.demirci
 * @date: 10.01.2021
 */

@ExperimentalCoroutinesApi
class HistoryViewModelTest {

  @get:Rule
  var instantExecutorRule = InstantTaskExecutorRule()

  @get:Rule
  var mainCoroutineRule = MainCoroutineRule()

  // Subject under test
  private lateinit var historyViewModel: HistoryViewModel

  @MockK
  lateinit var playerRepository: PlayerRepository
  @MockK
  lateinit var savedStateHandle: SavedStateHandle
  @MockK
  lateinit var userManager: UserManager

  @Before
  fun setUp() {
    MockKAnnotations.init(this, relaxed = true)
    historyViewModel = HistoryViewModel(
      savedStateHandle = savedStateHandle,
      playerRepository = playerRepository,
      userManager = userManager
    )
  }

  @Test
  fun fetchRecentlyPlayed_WhenError_ShouldFireErrorEvent() =
    mainCoroutineRule.runBlockingTest {
      val exception = Exception()
      val res = Result.Error(exception)

      coEvery { playerRepository.fetchRecentlyPlayed(any()) } returns res

      historyViewModel.fetchRecentlyPlayedSongs("") {}

      Truth.assertThat(historyViewModel.event.getOrAwaitValue().getContentIfNotHandled())
        .isInstanceOf(HistoryViewEvent.ShowError::class.java)
      Truth.assertThat(historyViewModel.urisOfTracks.isEmpty())
    }

  @Test
  fun fetchRecentlyPlayed_WhenSuccess_ShouldAddUriToList() =
    mainCoroutineRule.runBlockingTest {
      val res = Result.Success(mockk<HistoryResponse>())

      coEvery { playerRepository.fetchRecentlyPlayed(any()) } returns res

      historyViewModel.fetchRecentlyPlayedSongs("") {}
      Truth.assertThat(historyViewModel.urisOfTracks.isNotEmpty())
    }

  @Test
  fun onAddIconClick_WhenEasifyTrackIsNotNull_shouldSendOnAddIconClickEvent() =
    mainCoroutineRule.runBlockingTest {

      val easifyItemWithTrack = EasifyItem(type = EasifyItemType.TRACK, track = mockk())
      val easifyItemWithNullTrack = EasifyItem(type = EasifyItemType.TRACK)

      historyViewModel.onAddIconClick(easifyItemWithTrack)
      Truth.assertThat(historyViewModel.event.getOrAwaitValue().getContentIfNotHandled())
        .isInstanceOf(HistoryViewEvent.AddIconClicked::class.java)

      historyViewModel.onAddIconClick(easifyItemWithNullTrack)
      Truth.assertThat(historyViewModel.event.getOrAwaitValue().getContentIfNotHandled())
        .isNotInstanceOf(HistoryViewEvent.AddIconClicked::class.java)
    }
}
