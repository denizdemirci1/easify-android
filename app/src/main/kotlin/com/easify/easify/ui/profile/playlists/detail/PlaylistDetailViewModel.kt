package com.easify.easify.ui.profile.playlists.detail

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.easify.easify.data.remote.util.parseNetworkError
import com.easify.easify.data.repositories.PlaylistRepository
import com.easify.easify.model.PlaylistTrack
import com.easify.easify.model.RemoveTrackObject
import com.easify.easify.model.Result.Error
import com.easify.easify.model.Result.Success
import com.easify.easify.model.TracksToDelete
import com.easify.easify.model.util.EasifyItem
import com.easify.easify.model.util.EasifyPlaylist
import com.easify.easify.model.util.EasifyTrack
import com.easify.easify.ui.base.BaseViewModel
import com.easify.easify.ui.extensions.toEasifyItemList
import com.easify.easify.util.Event
import com.easify.easify.util.manager.UserManager
import kotlinx.coroutines.launch

/**
 * @author: deniz.demirci
 * @date: 10/3/2020
 */

/**
 * Max & Default track count to fetch at once
 * from spotify api for a playlist
 */
private const val MAX_ITEM_COUNT = 100

class PlaylistDetailViewModel @ViewModelInject constructor(
  @Assisted private val savedStateHandle: SavedStateHandle,
  private val playlistRepository: PlaylistRepository,
  private val userManager: UserManager
) : BaseViewModel(userManager) {

  // region variables
  private lateinit var playlist: EasifyPlaylist

  private val playlistsTracksToShow = ArrayList<PlaylistTrack>()
  private var requestCount = 0

  private val _title = MutableLiveData<String>()
  val title: LiveData<String> = _title

  private val _isEditable = MutableLiveData(false)
  val isEditable: LiveData<Boolean> = _isEditable

  // TODO: implement a view for empty playlists
  private val _showNoTracksLayout = MutableLiveData(false)
  val showNoTracksLayout: LiveData<Boolean> = _showNoTracksLayout

  private val _loading = MutableLiveData(false)
  val loading: LiveData<Boolean> = _loading

  private val _event = MutableLiveData<Event<PlaylistDetailViewEvent>>()
  val event: LiveData<Event<PlaylistDetailViewEvent>> = _event

  private fun sendEvent(event: PlaylistDetailViewEvent) {
    _event.value = Event(event)
  }
  // endregion

  fun initialize(playlist: EasifyPlaylist) {
    this.playlist = playlist
    _title.value = playlist.name
    _isEditable.value = playlist.ownerId == userManager.user?.id
    getPlaylistTracks(playlist.id)
  }

  private fun getPlaylistTracks(playlistId: String) {
    viewModelScope.launch {
      _loading.value = true
      playlistRepository.fetchPlaylistTracks(
        playlistId,
        requestCount * MAX_ITEM_COUNT
      ).let { result ->
        _loading.value = false
        when (result) {
          is Success -> {
            requestCount++
            playlistsTracksToShow.addAll(result.data.items)
            if (playlistsTracksToShow.size < result.data.total) {
              getPlaylistTracks(playlistId)
            } else {
              sendEvent(PlaylistDetailViewEvent.NotifyDataChanged(
                ArrayList(playlistsTracksToShow).map { it.track }.toEasifyItemList())
              )
              if (playlistsTracksToShow.isEmpty()) {
                _showNoTracksLayout.value = true
              }
            }
          }
          is Error -> {
            val message = parseNetworkError(result.exception, ::onAuthError)
            message?.let { sendEvent(PlaylistDetailViewEvent.ShowError(message)) }
          }
        }
      }
    }
  }

  private fun onAuthError() {
    sendEvent(PlaylistDetailViewEvent.Authenticate)
  }

  fun removeTrack(track: EasifyTrack) {
    playlistsTracksToShow.removeAll { it.track.id == track.id }
    removeTrackFromPlaylist(track)
  }

  private fun removeTrackFromPlaylist(track: EasifyTrack) {
    viewModelScope.launch {
      val tracksToDelete = listOf(TracksToDelete(uri = track.uri))
      playlistRepository.removeTracksFromPlaylist(playlist.id, RemoveTrackObject(tracksToDelete))
      sendEvent(PlaylistDetailViewEvent.ShowSnackbar(track.name))
      sendEvent(PlaylistDetailViewEvent.NotifyDataChanged(
        ArrayList(playlistsTracksToShow).map { it.track }.toEasifyItemList())
      )
    }
  }

  override fun onItemClick(item: EasifyItem, position: Int) {
    item.track?.let { track ->
      viewModelScope.launch {
        sendEvent(PlaylistDetailViewEvent.ListenIconClicked(track.uri))
        if (userManager.deviceId.isNullOrEmpty()) {
          sendEvent(PlaylistDetailViewEvent.GetDevices)
        } else {
          sendEvent(PlaylistDetailViewEvent.Play)
        }
      }
    }
  }
}
