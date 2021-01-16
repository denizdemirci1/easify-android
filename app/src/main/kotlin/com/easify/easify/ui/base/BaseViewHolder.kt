package com.easify.easify.ui.base

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * @author: deniz.demirci
 * @date: 9/13/2020
 */

/**
 * Base view holder to standardize and simplify initialization for this component.
 *
 * @param binding View data binding generated class instance.
 * @see RecyclerView.ViewHolder
 */
abstract class BaseViewHolder<T : ViewDataBinding>(
  val binding: T
) : RecyclerView.ViewHolder(binding.root)