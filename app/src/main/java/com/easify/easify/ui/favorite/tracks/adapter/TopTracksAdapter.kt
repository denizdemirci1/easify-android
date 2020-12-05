package com.easify.easify.ui.favorite.tracks.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.easify.easify.databinding.ViewHolderTopTracksBinding
import com.easify.easify.model.Track
import com.easify.easify.ui.base.BasePagedListAdapter
import com.easify.easify.ui.base.BaseViewHolder
import com.easify.easify.ui.favorite.tracks.TopTracksViewModel

/**
 * @author: deniz.demirci
 * @date: 9/27/2020
 */

class TopTracksAdapter(
  private val topTracksViewModel: TopTracksViewModel
) : BasePagedListAdapter<Track>(
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
        getItem(position)?.let { track -> holder.bind(track, topTracksViewModel) }
      }
    }
  }
}

class TopArtistsViewHolder(
  parent: ViewGroup,
  inflater: LayoutInflater
) : BaseViewHolder<ViewHolderTopTracksBinding>(
  binding = ViewHolderTopTracksBinding.inflate(inflater, parent, false)
) {

  fun bind(track: Track, topTracksViewModel: TopTracksViewModel) {
    binding.track = track
    binding.viewModel = topTracksViewModel
    binding.executePendingBindings()
  }
}
