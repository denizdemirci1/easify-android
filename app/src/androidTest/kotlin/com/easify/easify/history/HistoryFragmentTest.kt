package com.easify.easify.history

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.easify.easify.R
import com.easify.easify.launchFragmentInHiltContainer
import com.easify.easify.ui.common.adapter.EasifyItemPagedListAdapter
import com.easify.easify.ui.common.adapter.viewholder.TrackViewHolder
import com.easify.easify.ui.history.HistoryFragment
import com.easify.easify.ui.history.HistoryViewModel
import com.easify.easify.util.EspressoIdlingResource
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

/**
 * @author: deniz.demirci
 * @date: 17.01.2021
 */

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class HistoryFragmentTest {

  @get:Rule
  var hiltRule = HiltAndroidRule(this)

  @MockK
  lateinit var navController: NavController

  private lateinit var historyFragment: HistoryFragment

  @Before
  fun registerIdlingResource() {
    IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
  }

  @Before
  fun setup() {
    hiltRule.inject()
    MockKAnnotations.init(this, relaxed = true)
    launchFragmentInHiltContainer<HistoryFragment> {
      historyFragment = this
      Navigation.setViewNavController(requireView(), navController)
    }
  }

  @Test
  fun clickOnPlaylistLayout_shouldOpenPlaylistFragment() {
  }

  @After
  fun unregisterIdlingResource() {
    IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
  }
}
