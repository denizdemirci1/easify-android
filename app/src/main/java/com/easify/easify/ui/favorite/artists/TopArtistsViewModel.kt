package com.easify.easify.ui.favorite.artists

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.easify.easify.data.repositories.PersonalizationRepository

/**
 * @author: deniz.demirci
 * @date: 9/25/2020
 */

class TopArtistsViewModel @ViewModelInject constructor(
  @Assisted private val savedStateHandle: SavedStateHandle,
  private val personalizationRepository: PersonalizationRepository
) : ViewModel() {

}