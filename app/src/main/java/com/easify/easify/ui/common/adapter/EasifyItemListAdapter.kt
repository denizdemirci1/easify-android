package com.easify.easify.ui.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.easify.easify.model.util.EasifyItem
import com.easify.easify.model.util.EasifyItemType
import com.easify.easify.ui.base.BaseListAdapter
import com.easify.easify.ui.base.BaseViewModel
import com.easify.easify.ui.common.adapter.viewholder.*

/**
 * @author: deniz.demirci
 * @date: 12.12.2020
 */

class EasifyItemListAdapter(
  private val viewModel: BaseViewModel,
  private var removeListener: ((EasifyItem) -> Unit)? = null,
  private val horizontal: Boolean = false
) : BaseListAdapter<EasifyItem>(
  itemsSame = { old, new -> old == new },
  contentsSame = { old, new -> old == new }
) {

  override fun onCreateViewHolder(
    parent: ViewGroup,
    inflater: LayoutInflater,
    viewType: Int
  ): RecyclerView.ViewHolder {
    return when (viewType) {
      EasifyItemType.TRACK.value -> {
        if (horizontal) {
          TrackHorizontalViewHolder(parent, inflater)
        } else {
          TrackViewHolder(parent, inflater)
        }
      }
      EasifyItemType.ARTIST.value -> {
        if (horizontal) {
          ArtistHorizontalViewHolder(parent, inflater)
        } else {
          ArtistViewHolder(parent, inflater)
        }
      }
      EasifyItemType.PLAYLIST.value -> PlaylistViewHolder(parent, inflater)
      else -> EmptyViewHolder(parent, inflater)
    }
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    getItem(position)?.let { easifyItem ->
      when (holder) {
        is TrackViewHolder -> holder.bind(easifyItem, position, viewModel, removeListener)
        is TrackHorizontalViewHolder -> holder.bind(easifyItem, position, viewModel)
        is ArtistViewHolder -> holder.bind(easifyItem, position, viewModel)
        is ArtistHorizontalViewHolder -> holder.bind(easifyItem, position, viewModel)
        is PlaylistViewHolder -> holder.bind(easifyItem, position, viewModel)
        is EmptyViewHolder -> holder.bind()
      }
    }
  }

  override fun getItemViewType(position: Int): Int {
    return getItem(position)?.type?.value ?: -1
  }
}
