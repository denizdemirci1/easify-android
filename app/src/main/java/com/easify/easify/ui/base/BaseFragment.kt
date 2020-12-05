package com.easify.easify.ui.base

import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.easify.easify.R
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * @author: deniz.demirci
 * @date: 9/4/2020
 */

open class BaseFragment : Fragment {

  constructor() : super()
  constructor(@LayoutRes resId: Int) : super(resId)

  internal fun showBottomNavigation(show: Boolean) {
    if (show) {
      activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation_view)?.visibility =
        View.VISIBLE
    } else {
      activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation_view)?.visibility =
        View.GONE
    }
  }
}
