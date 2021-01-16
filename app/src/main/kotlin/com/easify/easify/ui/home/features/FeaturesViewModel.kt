package com.easify.easify.ui.home.features

import androidx.annotation.VisibleForTesting
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.easify.easify.data.remote.util.parseNetworkError
import com.easify.easify.data.repositories.TrackRepository
import com.easify.easify.model.Result
import com.easify.easify.ui.base.BaseViewModel
import com.easify.easify.util.Event
import com.easify.easify.util.manager.UserManager
import kotlinx.coroutines.launch

/**
 * @author: deniz.demirci
 * @date: 28.11.2020
 */

class FeaturesViewModel @ViewModelInject constructor(
  @Assisted private val savedStateHandle: SavedStateHandle,
  private val trackRepository: TrackRepository,
  userManager: UserManager
) : BaseViewModel(userManager) {

  private val _event = MutableLiveData<Event<FeaturesViewEvent>>()
  val event: LiveData<Event<FeaturesViewEvent>> = _event

  fun sendEvent(event: FeaturesViewEvent) {
    _event.value = Event(event)
  }

  fun getAudioFeatures(trackId: String) {
    viewModelScope.launch {
      trackRepository.fetchAudioFeatures(trackId).let { result ->
        when (result) {
          is Result.Success ->  sendEvent(FeaturesViewEvent.OnFeaturesReceived(result.data))
          is Result.Error -> {
            val message = parseNetworkError(result.exception, ::onAuthError)
            message?.let { sendEvent(FeaturesViewEvent.ShowError(message)) }
          }
        }
      }
    }
  }

  @VisibleForTesting
  fun onAuthError() {
    sendEvent(FeaturesViewEvent.Authenticate)
  }
}
