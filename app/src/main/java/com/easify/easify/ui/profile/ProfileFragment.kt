package com.easify.easify.ui.profile

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.easify.easify.R
import com.easify.easify.databinding.FragmentProfileBinding
import com.easify.easify.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_profile.*

/**
 * @author: deniz.demirci
 * @date: 9/5/2020
 */

@AndroidEntryPoint
class ProfileFragment : BaseFragment(R.layout.fragment_profile) {

  private val viewModel by viewModels<ProfileViewModel>()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    DataBindingUtil.bind<FragmentProfileBinding>(profileRoot)?.apply {
      lifecycleOwner = this@ProfileFragment.viewLifecycleOwner
      viewModel = this@ProfileFragment.viewModel
    }
  }
}
