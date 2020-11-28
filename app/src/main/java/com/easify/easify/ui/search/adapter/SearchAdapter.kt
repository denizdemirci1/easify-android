package com.easify.easify.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.easify.easify.databinding.ViewHolderSearchArtistBinding
import com.easify.easify.databinding.ViewHolderSearchTrackBinding
import com.easify.easify.model.Artist
import com.easify.easify.model.SearchType
import com.easify.easify.model.Track
import com.easify.easify.ui.base.BaseListAdapter
import com.easify.easify.ui.base.BaseViewHolder
import com.easify.easify.ui.search.SearchViewModel

/**
 * @author: deniz.demirci
 * @date: 28.11.2020
 */

class SearchAdapter<T>(
  private val searchViewModel: SearchViewModel,
  private val type: SearchType
) : BaseListAdapter<T>(
  itemsSame = { old, new ->
    when (type) {
      SearchType.TRACK -> (old as? Track)?.id == (new as? Track)?.id
      SearchType.ARTIST -> (old as? Artist)?.id == (new as? Artist)?.id
    }
  },
  contentsSame = { old, new -> old == new }
) {

  override fun onCreateViewHolder(
    parent: ViewGroup,
    inflater: LayoutInflater,
    viewType: Int
  ): RecyclerView.ViewHolder {
    return when (type) {
      SearchType.ARTIST -> SearchArtistViewHolder(parent, inflater)
      SearchType.TRACK -> SearchTrackViewHolder(parent, inflater)
    }
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    when (holder) {
      is SearchTrackViewHolder -> holder.bind(searchViewModel, (getItem(position) as? Track))
      is SearchArtistViewHolder -> holder.bind(searchViewModel, (getItem(position) as? Artist))
    }
  }
}

class SearchArtistViewHolder(
  parent: ViewGroup,
  inflater: LayoutInflater
) : BaseViewHolder<ViewHolderSearchArtistBinding>(
  binding = ViewHolderSearchArtistBinding.inflate(inflater, parent, false)
) {

  fun bind(searchViewModel: SearchViewModel, artist: Artist?) {
    binding.searchViewModel = searchViewModel
    binding.artist = artist
    binding.executePendingBindings()
  }
}

class SearchTrackViewHolder(
  parent: ViewGroup,
  inflater: LayoutInflater
) : BaseViewHolder<ViewHolderSearchTrackBinding>(
  binding = ViewHolderSearchTrackBinding.inflate(inflater, parent, false)
) {

  fun bind(searchViewModel: SearchViewModel, track: Track?) {
    binding.searchViewModel = searchViewModel
    binding.track = track
    binding.executePendingBindings()
  }
}
