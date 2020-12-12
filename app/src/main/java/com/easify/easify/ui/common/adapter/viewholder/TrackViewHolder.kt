package com.easify.easify.ui.common.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.easify.easify.databinding.ViewHolderEasifyTrackBinding
import com.easify.easify.model.util.EasifyItem
import com.easify.easify.ui.base.BaseViewHolder
import com.easify.easify.ui.base.BaseViewModel
import com.easify.easify.ui.extensions.animateFading

/**
 * @author: deniz.demirci
 * @date: 11.12.2020
 */

class TrackViewHolder(
  parent: ViewGroup,
  inflater: LayoutInflater
) : BaseViewHolder<ViewHolderEasifyTrackBinding>(
  binding = ViewHolderEasifyTrackBinding.inflate(inflater, parent, false)
) {

  fun bind(
    easifyItem: EasifyItem,
    position: Int,
    viewModel: BaseViewModel,
    removeListener: ((EasifyItem) -> Unit)?
  ) {
    binding.position = position
    binding.easifyItem = easifyItem
    binding.viewModel = viewModel
    binding.isRemovable = removeListener != null
    binding.removeTrack.setOnClickListener {
      binding.trackRoot.animateFading()
      removeListener?.invoke(easifyItem)
    }
    binding.executePendingBindings()
  }
}
