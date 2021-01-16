package com.easify.easify.ui.base

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.easify.easify.model.util.EasifyItem
import com.easify.easify.util.manager.UserManager

/**
 * @author: deniz.demirci
 * @date: 11.12.2020
 */

open class BaseViewModel @ViewModelInject constructor(
  private val userManager: UserManager
) : ViewModel() {

  var isListenIconNeededForTrack = false



  open fun onItemClick(item: EasifyItem, position: Int) = Unit

  open fun onAddIconClick(item: EasifyItem) = Unit

  open fun onListenIconClick(item: EasifyItem) = Unit

  fun saveToken(accessToken: String) {
    userManager.token = accessToken
  }

  fun clearToken() {
    userManager.token = ""
  }
}
