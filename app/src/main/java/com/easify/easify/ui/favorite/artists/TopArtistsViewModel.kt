package com.easify.easify.ui.favorite.artists

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.easify.easify.data.remote.util.parseNetworkError
import com.easify.easify.data.repositories.PersonalizationRepository
import com.easify.easify.model.Artist
import com.easify.easify.model.Result.Success
import com.easify.easify.model.Result.Error
import com.easify.easify.model.TopArtistResponse
import com.easify.easify.util.Event
import kotlinx.coroutines.launch

/**
 * @author: deniz.demirci
 * @date: 9/25/2020
 */

class TopArtistsViewModel @ViewModelInject constructor(
  @Assisted private val savedStateHandle: SavedStateHandle,
  private val personalizationRepository: PersonalizationRepository
) : ViewModel() {

  private val _loading = MutableLiveData(false)
  val loading: LiveData<Boolean> = _loading

  private val _event = MutableLiveData<Event<TopArtistsViewEvent>>()
  val event: LiveData<Event<TopArtistsViewEvent>> = _event

  private fun sendEvent(event: TopArtistsViewEvent) {
    _event.value = Event(event)
  }

  fun getTopArtists(
    timeRange: String,
    limit: Int,
    offset: Int,
    onSuccess: (data: TopArtistResponse) -> Unit
  ) {
    viewModelScope.launch {
      _loading.value = true
      personalizationRepository.fetchTopArtists(timeRange, limit, offset).let { result ->
        _loading.value = false
        when (result) {
          is Success -> result.data.let(onSuccess)
          is Error -> {
            sendEvent(TopArtistsViewEvent.ShowError(parseNetworkError(result.exception)))
          }
        }
      }
    }
  }

  fun openArtistFragment(artist: Artist) {

  }
}