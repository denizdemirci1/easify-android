package com.easify.easify.ui.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.easify.easify.model.util.EasifyItem
import com.easify.easify.model.util.EasifyItemType
import com.easify.easify.ui.base.BasePagedListAdapter
import com.easify.easify.ui.base.BaseViewModel
import com.easify.easify.ui.common.adapter.viewholder.ArtistViewHolder
import com.easify.easify.ui.common.adapter.viewholder.EmptyViewHolder
import com.easify.easify.ui.common.adapter.viewholder.PlaylistViewHolder
import com.easify.easify.ui.common.adapter.viewholder.TrackViewHolder

private const val TRACK = 0
private const val ARTIST = 1
private const val PLAYLIST = 2

/**
 * @author: deniz.demirci
 * @date: 11.12.2020
 */

class EasifyItemAdapter(
  private val viewModel: BaseViewModel
) : BasePagedListAdapter<EasifyItem>(
  itemsSame = { old, new -> old == new },
  contentsSame = { old, new -> old == new }
) {

  override fun onCreateViewHolder(
    parent: ViewGroup,
    inflater: LayoutInflater,
    viewType: Int
  ): RecyclerView.ViewHolder {
    return when (viewType) {
      TRACK -> TrackViewHolder(parent, inflater)
      ARTIST -> ArtistViewHolder(parent, inflater)
      PLAYLIST -> PlaylistViewHolder(parent, inflater)
      else -> EmptyViewHolder(parent, inflater)
    }
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    getItem(position)?.let { easifyItem ->
      when (holder) {
        is PlaylistViewHolder -> holder.bind(easifyItem, position, viewModel)
        is EmptyViewHolder -> holder.bind()
      }
    }
  }

  override fun getItemViewType(position: Int): Int {
    return when (getItem(position)?.type) {
      EasifyItemType.TRACK -> TRACK
      EasifyItemType.ARTIST -> ARTIST
      EasifyItemType.PLAYLIST -> PLAYLIST
      else -> -1
    }
  }
}
