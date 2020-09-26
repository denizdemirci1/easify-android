package com.easify.easify.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.ads.MobileAds
import com.easify.easify.R
import com.easify.easify.ui.extensions.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @author: deniz.demirci
 * @date: 9/4/2020
 */

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  private var currentNavController: LiveData<NavController>? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    MobileAds.initialize(this)

    if (savedInstanceState == null) {
      setupBottomNavigationBar()
    }
  }

  override fun onRestoreInstanceState(savedInstanceState: Bundle) {
    super.onRestoreInstanceState(savedInstanceState)
    setupBottomNavigationBar()
  }

  private fun setupBottomNavigationBar() {
    val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
    val navGraphIds = listOf(
      R.navigation.home_nav_graph,
      R.navigation.history_nav_graph,
      R.navigation.favorites_nav_graph,
      R.navigation.profile_nav_graph
    )
    val controller = bottomNavigationView.setupWithNavController(
      navGraphIds = navGraphIds,
      fragmentManager = supportFragmentManager,
      containerId = R.id.container_fragment
    )
    currentNavController = controller
  }

  override fun onSupportNavigateUp(): Boolean {
    return currentNavController?.value?.navigateUp() ?: false
  }

  override fun onBackPressed() {
    if (isOnHomeFragment()) {
      finish()
    } else if (isOnRootFragment() && !isOnHomeTab()) {
      bottom_navigation_view.selectedItemId = R.id.home_nav_graph
    } else if (!isOnRootFragment()) {
      findNavController(R.id.container_fragment).popBackStack()
    } else {
      super.onBackPressed()
    }
  }

  private fun isOnHomeFragment() =
    findNavController(R.id.container_fragment).let { navController ->
      navController.currentDestination?.id ?: 0 == R.id.homeFragment
    }

  private fun isOnRootFragment() =
    findNavController(R.id.container_fragment).let { navController ->
      navController.graph.startDestination == navController.currentDestination?.id ?: 0
    }

  private fun isOnHomeTab() =
    bottom_navigation_view.selectedItemId == R.id.home_nav_graph

  /**
   * To be able to call onActivityResult from SplashFragment for Spotify Callback
   */
  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    val navHostFragment =
      supportFragmentManager.findFragmentByTag("bottomNavigation#0") as NavHostFragment?
    val splashFragment = navHostFragment?.childFragmentManager?.fragments?.get(0)
    splashFragment?.onActivityResult(requestCode, resultCode, data)
  }
}
