package com.easify.easify.ui.splash

import androidx.annotation.VisibleForTesting
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.easify.easify.data.remote.util.parseNetworkError
import com.easify.easify.data.repositories.UserRepository
import com.easify.easify.model.Result.Error
import com.easify.easify.model.Result.Success
import com.easify.easify.ui.base.BaseViewModel
import com.easify.easify.util.Event
import com.easify.easify.util.manager.UserManager
import kotlinx.coroutines.launch

/**
 * @author: deniz.demirci
 * @date: 9/5/2020
 */

class SplashViewModel @ViewModelInject constructor(
  @Assisted private val savedStateHandle: SavedStateHandle,
  private val userManager: UserManager,
  private val userRepository: UserRepository
) : BaseViewModel(userManager) {

  private val _event = MutableLiveData<Event<SplashViewEvent>>()
  val event: LiveData<Event<SplashViewEvent>> = _event

  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  fun sendEvent(event: SplashViewEvent) {
    _event.value = Event(event)
  }

  fun fetchUser() {
    viewModelScope.launch {
      userRepository.fetchUser().let { result ->
        when (result) {
          is Success -> {
            userManager.user = result.data
            userManager.tokenRefreshed = false
            sendEvent(SplashViewEvent.OpenHomePage)
          }
          is Error -> {
            val message = parseNetworkError(result.exception, ::onAuthError)
            message?.let { sendEvent(SplashViewEvent.ShowError(message)) }
          }
        }
      }
    }
  }

  private fun onAuthError() {
    sendEvent(SplashViewEvent.Authenticate)
  }

  fun authenticateSpotify() {
    if (userManager.token.isNullOrEmpty()) {
      sendEvent(SplashViewEvent.Authenticate)
    } else {
      fetchUser()
    }
  }

  /***
   * If the auto authentication failed, try to refresh token
   * by authenticating again before showing error message.
   * If it fails again, then show the error.
   */
  fun handleAuthError(message: String?) {
    if (userManager.tokenRefreshed) {
      sendEvent(SplashViewEvent.ShowError(message))
      userManager.tokenRefreshed = false
    } else {
      userManager.tokenRefreshed = false
      authenticateSpotify()
    }
  }
}
