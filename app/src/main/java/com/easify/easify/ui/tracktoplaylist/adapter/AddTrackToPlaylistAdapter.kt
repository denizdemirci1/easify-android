package com.easify.easify.ui.tracktoplaylist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.easify.easify.databinding.ViewHolderPlaylistToAddBinding
import com.easify.easify.model.Playlist
import com.easify.easify.ui.base.BasePagedListAdapter
import com.easify.easify.ui.base.BaseViewHolder
import com.easify.easify.ui.tracktoplaylist.AddTrackToPlaylistViewModel

/**
 * @author: deniz.demirci
 * @date: 2.11.2020
 */

class AddTrackToPlaylistAdapter(
  private val addTrackToPlaylistViewModel: AddTrackToPlaylistViewModel
) : BasePagedListAdapter<Playlist>(
  itemsSame = { old, new -> old == new },
  contentsSame = { old, new -> old.id == new.id }
) {

  override fun onCreateViewHolder(
    parent: ViewGroup,
    inflater: LayoutInflater,
    viewType: Int
  ): RecyclerView.ViewHolder {
    return PlaylistToAddViewHolder(parent, inflater)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    when (holder) {
      is PlaylistToAddViewHolder -> {
        getItem(position)?.let { playlist -> holder.bind(playlist, addTrackToPlaylistViewModel) }
      }
    }
  }
}

class PlaylistToAddViewHolder(
  parent: ViewGroup,
  inflater: LayoutInflater
) : BaseViewHolder<ViewHolderPlaylistToAddBinding>(
  binding = ViewHolderPlaylistToAddBinding.inflate(inflater, parent, false)
) {

  fun bind(playlist: Playlist, addTrackToPlaylistViewModel: AddTrackToPlaylistViewModel) {
    binding.playlist = playlist
    binding.addTrackToPlaylistViewModel = addTrackToPlaylistViewModel
    binding.executePendingBindings()
  }
}
