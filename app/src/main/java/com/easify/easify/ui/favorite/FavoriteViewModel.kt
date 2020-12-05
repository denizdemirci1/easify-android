package com.easify.easify.ui.favorite

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

/**
 * @author: deniz.demirci
 * @date: 9/18/2020
 */

class FavoriteViewModel @ViewModelInject constructor(
  @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

}