package com.easify.easify.ui.profile.playlists.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.easify.easify.R
import com.easify.easify.databinding.ViewHolderPlaylistDetailBinding
import com.easify.easify.model.PlaylistTrack
import com.easify.easify.ui.base.BasePagedListAdapter
import com.easify.easify.ui.base.BaseViewHolder
import com.easify.easify.ui.profile.playlists.detail.PlaylistDetailViewModel

/**
 * @author: deniz.demirci
 * @date: 10/3/2020
 */

class PlaylistDetailAdapter(
  private val playlistDetailViewModel: PlaylistDetailViewModel
) : BasePagedListAdapter<PlaylistTrack>(
  itemsSame = { old, new -> old == new },
  contentsSame = { old, new -> old.track.id == new.track.id }
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
      is PlaylistDetailViewHolder -> {
        getItem(position)?.let { playlistTrack ->
          holder.bind(playlistTrack, position, playlistDetailViewModel)
        }
      }
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
    playlistTrack: PlaylistTrack,
    position: Int,
    playlistDetailViewModel: PlaylistDetailViewModel
  ) {
    binding.playlistTrack = playlistTrack
    binding.position = position
    binding.playlistDetailViewModel = playlistDetailViewModel
    binding.remove.setOnClickListener {
      playlistDetailViewModel.onRemoveClicked(playlistTrack.track)
      binding.remove.isClickable = false
      binding.trackRoot.isClickable = false
      binding.trackRoot.setBackgroundColor(
        ContextCompat.getColor(itemView.context, R.color.colorBackgroundLight)
      )
    }
    binding.executePendingBindings()
  }
}
