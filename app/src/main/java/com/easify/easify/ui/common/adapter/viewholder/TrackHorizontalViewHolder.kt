package com.easify.easify.ui.common.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.easify.easify.databinding.ViewHolderEasifyTrackHorizontalBinding
import com.easify.easify.model.util.EasifyItem
import com.easify.easify.ui.base.BaseViewHolder
import com.easify.easify.ui.base.BaseViewModel

/**
 * @author: deniz.demirci
 * @date: 19.12.2020
 */

class TrackHorizontalViewHolder(
  parent: ViewGroup,
  inflater: LayoutInflater
) : BaseViewHolder<ViewHolderEasifyTrackHorizontalBinding>(
  binding = ViewHolderEasifyTrackHorizontalBinding.inflate(inflater, parent, false)
) {

  fun bind(
    easifyItem: EasifyItem,
    position: Int,
    viewModel: BaseViewModel
  ) {
    binding.position = position
    binding.easifyItem = easifyItem
    binding.viewModel = viewModel
    binding.executePendingBindings()
  }
}
