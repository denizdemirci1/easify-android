package com.easify.easify.ui.search

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.easify.easify.data.remote.util.parseNetworkError
import com.easify.easify.data.repositories.SearchRepository
import com.easify.easify.model.*
import com.easify.easify.model.util.EasifyItem
import com.easify.easify.model.util.EasifyItemType
import com.easify.easify.ui.base.BaseViewModel
import com.easify.easify.ui.extensions.toEasifyItemList
import com.easify.easify.util.Event
import com.easify.easify.util.manager.UserManager
import kotlinx.coroutines.launch

/**
 * @author: deniz.demirci
 * @date: 28.11.2020
 */

class SearchViewModel @ViewModelInject constructor(
  @Assisted private val savedStateHandle: SavedStateHandle,
  private val searchRepository: SearchRepository,
  private val userManager: UserManager
) : BaseViewModel() {

  init {
    isListenIconNeededForTrack = true
  }

  private val tracksToShow = ArrayList<Track>()
  private val artistsToShow = ArrayList<Artist>()

  private val _event = MutableLiveData<Event<SearchViewEvent>>()
  val event: LiveData<Event<SearchViewEvent>> = _event

  fun sendEvent(event: SearchViewEvent) {
    _event.value = Event(event)
  }

  fun search(type: SearchType, query: String) {
    viewModelScope.launch {
      searchRepository.search(type, query).let { result ->
        when (result) {
          is Result.Success -> handleSearchResult(type, result.data, query)
          is Result.Error -> {
            sendEvent(SearchViewEvent.ShowError(parseNetworkError(result.exception)))
          }
        }
      }
    }
  }

  private fun handleSearchResult(type: SearchType, data: SearchResponse, query: String) {
    when (type) {
      SearchType.TRACK -> data.tracks?.items?.let { handleRetrievedTracks(it, query) }
      SearchType.ARTIST -> data.artists?.items?.let { handleRetrievedArtist(it) }
    }
  }

  private fun handleRetrievedTracks(tracks: List<Track>, query: String) {
    tracksToShow.clear()
    tracksToShow.addAll(tracks)
    tracksToShow.filter { track ->
      track.name.contains(query) || track.artists[0].name.contains(query)
    }
    sendEvent(SearchViewEvent.NotifyTrackDataChanged(ArrayList(tracksToShow.toEasifyItemList())))
  }

  private fun handleRetrievedArtist(tracks: List<Artist>) {
    artistsToShow.clear()
    artistsToShow.addAll(tracks)
    sendEvent(SearchViewEvent.NotifyArtistDataChanged(ArrayList(artistsToShow.toEasifyItemList())))
  }

  override fun onAddIconClick(item: EasifyItem) {
    when (item.type) {
      EasifyItemType.TRACK -> item.track?.let { track ->
        sendEvent(SearchViewEvent.OnAddIconClicked(track))
      }
      else -> Unit
    }
  }

  override fun onItemClick(item: EasifyItem, position: Int) {
    when (item.type) {
      EasifyItemType.TRACK -> item.track?.let { track ->
        sendEvent(SearchViewEvent.OnTrackClicked(track))
      }
      EasifyItemType.ARTIST -> item.artist?.let { artist ->
        sendEvent(SearchViewEvent.OnArtistClicked(artist))
      }
      else -> Unit
    }
  }

  override fun onListenIconClick(item: EasifyItem) {
    val uri = when (item.type) {
      EasifyItemType.TRACK -> item.track?.uri
      EasifyItemType.ARTIST -> item.artist?.uri
      else -> null
    }
    uri?.let { safeUri ->
      viewModelScope.launch {
        sendEvent(SearchViewEvent.OnListenIconClicked(safeUri))
        if (userManager.deviceId.isNullOrEmpty()) {
          sendEvent(SearchViewEvent.GetDevices)
        } else {
          sendEvent(SearchViewEvent.Play)
        }
      }
    }
  }
}
