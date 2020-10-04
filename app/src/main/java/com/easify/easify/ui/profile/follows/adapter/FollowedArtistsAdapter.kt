package com.easify.easify.ui.profile.follows.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.easify.easify.databinding.ViewHolderFollowedArtistsBinding
import com.easify.easify.model.Artist
import com.easify.easify.ui.base.BasePagedListAdapter
import com.easify.easify.ui.base.BaseViewHolder
import com.easify.easify.ui.profile.follows.FollowedArtistsViewModel

/**
 * @author: deniz.demirci
 * @date: 10/4/2020
 */

class FollowedArtistsAdapter(
  private val followedArtistsViewModel: FollowedArtistsViewModel
) : BasePagedListAdapter<Artist>(
  itemsSame = { old, new -> old == new },
  contentsSame = { old, new -> old.id == new.id }
) {

  override fun onCreateViewHolder(
    parent: ViewGroup,
    inflater: LayoutInflater,
    viewType: Int
  ): RecyclerView.ViewHolder {
    return FollowedArtistsViewHolder(parent, inflater)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    when (holder) {
      is FollowedArtistsViewHolder -> {
        getItem(position)?.let { artist -> holder.bind(artist, followedArtistsViewModel) }
      }
    }
  }
}

class FollowedArtistsViewHolder(
  parent: ViewGroup,
  inflater: LayoutInflater
) : BaseViewHolder<ViewHolderFollowedArtistsBinding>(
  binding = ViewHolderFollowedArtistsBinding.inflate(inflater, parent, false)
) {

  fun bind(artist: Artist, followedArtistsViewModel: FollowedArtistsViewModel) {
    binding.artist = artist
    binding.followedArtistsViewModel = followedArtistsViewModel
    binding.executePendingBindings()
  }
}
