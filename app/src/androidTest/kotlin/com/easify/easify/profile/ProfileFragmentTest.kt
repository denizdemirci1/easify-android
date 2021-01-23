package com.easify.easify.profile

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.easify.easify.R
import com.easify.easify.launchFragmentInHiltContainer
import com.easify.easify.ui.profile.ProfileFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * @author: deniz.demirci
 * @date: 17.01.2021
 */

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class ProfileFragmentTest {

  @MockK
  lateinit var navController: NavController

  @get:Rule
  var hiltRule = HiltAndroidRule(this)

  // Subject under test
  private lateinit var profileFragment: ProfileFragment

  @Before
  fun setup() {
    hiltRule.inject()
    MockKAnnotations.init(this, relaxed = true)
    launchFragmentInHiltContainer<ProfileFragment> {
      profileFragment = this
      Navigation.setViewNavController(requireView(), navController)
    }
  }

  @Test
  fun clickOnPlaylistLayout_shouldOpenPlaylistFragment() {
    onView(withId(R.id.playlists_layout)).perform(click())
    verify { navController.navigate(R.id.playlistFragment) }
  }

  @Test
  fun clickOnFollowedArtistsLayout_shouldOpenFollowedArtistsFragment() {
    onView(withId(R.id.followed_artists_layout)).perform(click())
    verify { navController.navigate(R.id.followedArtistsFragment) }
  }
}
