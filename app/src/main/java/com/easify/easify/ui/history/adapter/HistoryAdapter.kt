package com.easify.easify.ui.history.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.easify.easify.databinding.ViewHolderHistoryBinding
import com.easify.easify.model.History
import com.easify.easify.model.Track
import com.easify.easify.ui.base.BasePagedListAdapter
import com.easify.easify.ui.base.BaseViewHolder
import com.easify.easify.ui.history.HistoryViewModel

/**
 * @author: deniz.demirci
 * @date: 9/13/2020
 */

class HistoryAdapter(
  private val historyViewModel: HistoryViewModel
) : BasePagedListAdapter<History>(
  itemsSame = { old, new -> old.track.id == new.track.id },
  contentsSame = { old, new -> old.track.id == new.track.id }
) {

  override fun onCreateViewHolder(
    parent: ViewGroup,
    inflater: LayoutInflater,
    viewType: Int
  ): RecyclerView.ViewHolder {
    return HistoryViewHolder(parent, inflater)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    when (holder) {
      is HistoryViewHolder -> {
        getItem(position)?.let { history -> holder.bind(history.track, historyViewModel) }
      }
    }
  }
}

class HistoryViewHolder(
  parent: ViewGroup,
  inflater: LayoutInflater
) : BaseViewHolder<ViewHolderHistoryBinding>(
  binding = ViewHolderHistoryBinding.inflate(inflater, parent, false)
) {

  fun bind(track: Track?, historyViewModel: HistoryViewModel) {
    binding.track = track
    binding.historyViewModel = historyViewModel
    binding.executePendingBindings()
  }
}