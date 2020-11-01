package com.easify.easify.ui.favorite.artists.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.easify.easify.databinding.ViewHolderTopArtistsBinding
import com.easify.easify.model.Artist
import com.easify.easify.ui.base.BasePagedListAdapter
import com.easify.easify.ui.base.BaseViewHolder
import com.easify.easify.ui.favorite.artists.TopArtistsViewModel

/**
 * @author: deniz.demirci
 * @date: 9/26/2020
 */

class TopArtistsAdapter(
  private val topArtistsViewModel: TopArtistsViewModel
) : BasePagedListAdapter<Artist>(
  itemsSame = { old, new -> old == new },
  contentsSame = { old, new -> old.id == new.id }
) {

  override fun onCreateViewHolder(
    parent: ViewGroup,
    inflater: LayoutInflater,
    viewType: Int
  ): RecyclerView.ViewHolder {
    return TopArtistsViewHolder(parent, inflater)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    when (holder) {
      is TopArtistsViewHolder -> {
        getItem(position)?.let { artist -> holder.bind(artist, topArtistsViewModel) }
      }
    }
  }
}

class TopArtistsViewHolder(
  parent: ViewGroup,
  inflater: LayoutInflater
) : BaseViewHolder<ViewHolderTopArtistsBinding>(
  binding = ViewHolderTopArtistsBinding.inflate(inflater, parent, false)
) {

  fun bind(artist: Artist, topArtistsViewModel: TopArtistsViewModel) {
    binding.artist = artist
    binding.topArtistsViewModel = topArtistsViewModel
    binding.executePendingBindings()
  }
}
