package com.easify.easify.ui.profile.follows.followed.artist

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.easify.easify.data.remote.util.parseNetworkError
import com.easify.easify.data.repositories.FollowRepository
import com.easify.easify.data.repositories.PlayerRepository
import com.easify.easify.model.Artist
import com.easify.easify.model.Result.Success
import com.easify.easify.model.Result.Error
import com.easify.easify.model.util.EasifyArtist
import com.easify.easify.util.Event
import kotlinx.coroutines.launch

/**
 * @author: deniz.demirci
 * @date: 29.10.2020
 */

class ArtistViewModel @ViewModelInject constructor(
  @Assisted private val savedStateHandle: SavedStateHandle,
  private val followRepository: FollowRepository,
  private val playerRepository: PlayerRepository
) : ViewModel() {

  enum class FollowStatus {
    FOLLOW, UNFOLLOW
  }

  private var artist: EasifyArtist? = null

  private val _artists = MutableLiveData<List<Artist>>()
  val artists: LiveData<List<Artist>> = _artists

  private val _showFollowButton = MutableLiveData<Boolean>()
  val showFollowButton: LiveData<Boolean> = _showFollowButton

  private val _event = MutableLiveData<Event<ArtistViewEvent>>()
  val event: LiveData<Event<ArtistViewEvent>> = _event

  private fun sendEvent(event: ArtistViewEvent) {
    _event.value = Event(event)
  }

  fun setArtist(artist: EasifyArtist?) {
    this.artist = artist
  }

  fun fetchFollowedArtists() {
    viewModelScope.launch {
      followRepository.getFollowedArtists(50,null).let { result ->
        when (result) {
          is Success -> {
            _artists.value = result.data.artists.items
            setButtonVisibilities(result.data.artists.items)
          }
          is Error -> {
            sendEvent(ArtistViewEvent.ShowError(parseNetworkError(result.exception)))
          }
        }
      }
    }
  }

  fun followArtist(id: String) {
    viewModelScope.launch {
      followRepository.followArtist(id = id)
      sendEvent(ArtistViewEvent.ShowSnackbar(FollowStatus.FOLLOW))
      _showFollowButton.value = false
    }
  }

  fun unfollowArtist(id: String) {
    viewModelScope.launch {
      followRepository.unfollowArtist(id = id)
      sendEvent(ArtistViewEvent.ShowSnackbar(FollowStatus.UNFOLLOW))
      _showFollowButton.value = true
    }
  }

  private fun setButtonVisibilities(artists: List<Artist>) {
    artist?.let { easifyArtist ->
      val isArtistFollowed = artists.map { it.id }.contains(easifyArtist.id)
      _showFollowButton.value = !isArtistFollowed
    }
  }
}
