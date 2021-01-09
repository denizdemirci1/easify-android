package com.easify.easify.ui.profile

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.easify.easify.ui.base.BaseViewModel
import com.easify.easify.util.manager.UserManager

/**
 * @author: deniz.demirci
 * @date: 9/8/2020
 */

class ProfileViewModel @ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    private val userManager: UserManager
) : BaseViewModel(userManager) {

  fun getUser() = userManager.user
}
