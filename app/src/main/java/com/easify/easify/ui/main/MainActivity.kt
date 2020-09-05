package com.easify.easify.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.findNavController
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
    if (isOnRootFragment() && !isOnHomeTab()) {
      bottom_navigation_view.selectedItemId = R.id.home_nav_graph
    } else if (!isOnRootFragment()) {
      findNavController(R.id.container_fragment).popBackStack()
    } else {
      super.onBackPressed()
    }
  }

  private fun isOnRootFragment() =
    findNavController(R.id.container_fragment).let { navController ->
      navController.graph.startDestination == navController.currentDestination?.id ?: 0
    }

  private fun isOnHomeTab() =
    bottom_navigation_view.selectedItemId == R.id.home_nav_graph
}
