package com.easify.easify.ui.profile.playlists.create

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.easify.easify.data.remote.util.parseNetworkError
import com.easify.easify.data.repositories.PlaylistRepository
import com.easify.easify.model.CreatePlaylistBody
import com.easify.easify.model.Result
import com.easify.easify.util.Event
import com.easify.easify.util.manager.UserManager
import kotlinx.coroutines.launch

/**
 * @author: deniz.demirci
 * @date: 10/3/2020
 */

class CreatePlaylistViewModel @ViewModelInject constructor(
  @Assisted private val savedStateHandle: SavedStateHandle,
  private val playlistRepository: PlaylistRepository,
  private val userManager: UserManager
) : ViewModel() {

  private val _event = MutableLiveData<Event<CreatePlaylistViewEvent>>()
  val event: LiveData<Event<CreatePlaylistViewEvent>> = _event

  private fun sendEvent(event: CreatePlaylistViewEvent) {
    _event.value = Event(event)
  }

  fun createPlaylist(name: String, description: String) {
    userManager.user?.id?.let { userId ->
      viewModelScope.launch {
        playlistRepository.createPlaylist(
          userId,
          CreatePlaylistBody(name, description)
        ).let { result ->
          when (result) {
            is Result.Success -> sendEvent(CreatePlaylistViewEvent.Navigate)
            is Result.Error -> {
              sendEvent(CreatePlaylistViewEvent.ShowError(parseNetworkError(result.exception)))
            }
          }
        }
      }
    } ?: sendEvent(CreatePlaylistViewEvent.ShowUserIdNotFoundError)
  }
}
