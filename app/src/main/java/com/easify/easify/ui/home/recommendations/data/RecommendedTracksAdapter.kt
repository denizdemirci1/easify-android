package com.easify.easify.ui.home.recommendations.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.easify.easify.databinding.ViewHolderRecommendedTracksBinding
import com.easify.easify.model.Track
import com.easify.easify.ui.base.BaseListAdapter
import com.easify.easify.ui.base.BaseViewHolder
import com.easify.easify.ui.home.recommendations.RecommendationsViewModel

/**
 * @author: deniz.demirci
 * @date: 29.11.2020
 */

class RecommendedTracksAdapter(
  private val recommendationsViewModel: RecommendationsViewModel
) : BaseListAdapter<Track>(
  itemsSame = { old, new -> old.id == new.id },
  contentsSame = { old, new -> old == new }
) {

  override fun onCreateViewHolder(
    parent: ViewGroup,
    inflater: LayoutInflater,
    viewType: Int
  ): RecyclerView.ViewHolder {
    return RecommendedTracksViewHolder(parent, inflater)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    when (holder) {
      is RecommendedTracksViewHolder -> holder.bind(recommendationsViewModel, getItem(position))
    }
  }
}

class RecommendedTracksViewHolder(
  parent: ViewGroup,
  inflater: LayoutInflater
) : BaseViewHolder<ViewHolderRecommendedTracksBinding>(
  binding = ViewHolderRecommendedTracksBinding.inflate(inflater, parent, false)
) {

  fun bind(recommendationsViewModel: RecommendationsViewModel, track: Track) {
    binding.recommendationsViewModel = recommendationsViewModel
    binding.track = track
    binding.executePendingBindings()
  }
}
