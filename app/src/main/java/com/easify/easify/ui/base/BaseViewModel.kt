package com.easify.easify.ui.base

import androidx.lifecycle.ViewModel
import com.easify.easify.model.util.EasifyItem

/**
 * @author: deniz.demirci
 * @date: 11.12.2020
 */

open class BaseViewModel : ViewModel() {

  open fun onItemClick(item: EasifyItem, position: Int) = Unit

  open fun onAddIconClick(item: EasifyItem) = Unit

  open fun onListenIconClick(item: EasifyItem) = Unit
}
