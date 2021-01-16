package com.easify.easify.ui.common.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.easify.easify.databinding.ViewHolderEmptyBinding
import com.easify.easify.ui.base.BaseViewHolder

/**
 * @author: deniz.demirci
 * @date: 11.12.2020
 */

class EmptyViewHolder(
  parent: ViewGroup,
  inflater: LayoutInflater
) : BaseViewHolder<ViewHolderEmptyBinding>(
  binding = ViewHolderEmptyBinding.inflate(inflater, parent, false)
) {

  fun bind() {}
}