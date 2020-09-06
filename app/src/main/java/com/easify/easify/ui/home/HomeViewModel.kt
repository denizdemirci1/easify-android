package com.easify.easify.ui.home

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.easify.easify.util.manager.UserManager

/**
 * @author: deniz.demirci
 * @date: 9/5/2020
 */

class HomeViewModel @ViewModelInject constructor(
  @Assisted private val savedStateHandle: SavedStateHandle,
  private val userManager: UserManager
) : ViewModel() {

}
