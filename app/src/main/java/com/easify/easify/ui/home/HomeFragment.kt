package com.easify.easify.ui.home

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.easify.easify.R
import com.easify.easify.databinding.FragmentHomeBinding
import com.easify.easify.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * @author: deniz.demirci
 * @date: 9/4/2020
 */

@AndroidEntryPoint
class HomeFragment : BaseFragment(R.layout.fragment_home) {

  private val viewModel by viewModels<HomeViewModel>()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    DataBindingUtil.bind<FragmentHomeBinding>(homeRoot)?.apply {
      viewModel = this@HomeFragment.viewModel
    }
    showBottomNavigation(true)
  }
}
