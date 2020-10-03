package com.easify.easify.ui.profile.playlists.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.easify.easify.databinding.ViewHolderPlaylistDetailBinding
import com.easify.easify.model.PlaylistTrack
import com.easify.easify.model.Track
import com.easify.easify.ui.base.BaseListAdapter
import com.easify.easify.ui.base.BaseViewHolder
import com.easify.easify.ui.extensions.animateFading
import com.easify.easify.ui.profile.playlists.detail.PlaylistDetailViewModel

/**
 * @author: deniz.demirci
 * @date: 10/3/2020
 */

class PlaylistDetailAdapter(
  private val viewModel: PlaylistDetailViewModel,
  private var removeListener: ((Track) -> Unit)? = null
) : BaseListAdapter<PlaylistTrack>(
  itemsSame = { old, new -> old.track.id == new.track.id },
  contentsSame = { old, new -> old == new }
) {

  override fun onCreateViewHolder(
    parent: ViewGroup,
    inflater: LayoutInflater,
    viewType: Int
  ): RecyclerView.ViewHolder {
    return PlaylistDetailViewHolder(parent, inflater)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    when (holder) {
      is PlaylistDetailViewHolder -> holder.bind(viewModel, getItem(position), removeListener)
    }
  }
}

class PlaylistDetailViewHolder(
  parent: ViewGroup,
  inflater: LayoutInflater
) : BaseViewHolder<ViewHolderPlaylistDetailBinding>(
  binding = ViewHolderPlaylistDetailBinding.inflate(inflater, parent, false)
) {

  fun bind(
    playlistDetailViewModel: PlaylistDetailViewModel,
    playlistTrack: PlaylistTrack,
    removeListener: ((Track) -> Unit)?
  ) {
    binding.playlistDetailViewModel = playlistDetailViewModel
    binding.playlistTrack = playlistTrack
    binding.remove.setOnClickListener {
      binding.trackRoot.animateFading()
      removeListener?.invoke(playlistTrack.track)
    }
    binding.executePendingBindings()
  }
}
