package com.easify.easify.ui.splash

import androidx.annotation.VisibleForTesting
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.easify.easify.data.remote.util.parseNetworkError
import com.easify.easify.data.repositories.UserRepository
import com.easify.easify.model.Result.Error
import com.easify.easify.model.Result.Success
import com.easify.easify.util.Event
import com.easify.easify.util.manager.UserManager
import kotlinx.coroutines.launch
import java.util.*

/**
 * @author: deniz.demirci
 * @date: 9/5/2020
 */

class SplashViewModel @ViewModelInject constructor(
  @Assisted private val savedStateHandle: SavedStateHandle,
  private val userManager: UserManager,
  private val userRepository: UserRepository
) : ViewModel() {

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
            userManager.token = null
            handleAuthError(parseNetworkError(result.exception))
          }
        }
      }
    }
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
  fun handleAuthError(message: String) {
    if (userManager.tokenRefreshed) {
      sendEvent(SplashViewEvent.ShowError(message))
      userManager.tokenRefreshed = false
    } else {
      userManager.tokenRefreshed = false
      authenticateSpotify()
    }
  }

  fun saveToken(accessToken: String) {
    userManager.token = accessToken
  }

  fun clearToken() {
    userManager.token = ""
  }
}
