package com.easify.easify.ui.profile.playlists.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.easify.easify.databinding.ViewHolderPlaylistBinding
import com.easify.easify.model.Playlist
import com.easify.easify.ui.base.BasePagedListAdapter
import com.easify.easify.ui.base.BaseViewHolder
import com.easify.easify.ui.profile.playlists.PlaylistViewModel

/**
 * @author: deniz.demirci
 * @date: 9/29/2020
 */

class PlaylistAdapter(
  private val playlistViewModel: PlaylistViewModel
) : BasePagedListAdapter<Playlist>(
  itemsSame = { old, new -> old == new },
  contentsSame = { old, new -> old.id == new.id }
) {

  override fun onCreateViewHolder(
    parent: ViewGroup,
    inflater: LayoutInflater,
    viewType: Int
  ): RecyclerView.ViewHolder {
    return PlaylistViewHolder(parent, inflater)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    when (holder) {
      is PlaylistViewHolder -> {
        getItem(position)?.let { playlist -> holder.bind(playlist, playlistViewModel) }
      }
    }
  }
}

class PlaylistViewHolder(
  parent: ViewGroup,
  inflater: LayoutInflater
) : BaseViewHolder<ViewHolderPlaylistBinding>(
  binding = ViewHolderPlaylistBinding.inflate(inflater, parent, false)
) {

  fun bind(playlist: Playlist, playlistViewModel: PlaylistViewModel) {
    binding.playlist = playlist
    binding.viewModel = playlistViewModel
    binding.executePendingBindings()
  }
}
