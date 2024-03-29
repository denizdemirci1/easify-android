package com.easify.easify.ui.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

/**
 * @author: deniz.demirci
 * @date: 9/19/2020
 */

abstract class BasePagedListAdapter<T>(
  itemsSame: (T, T) -> Boolean,
  contentsSame: (T, T) -> Boolean
) : PagedListAdapter<T, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<T>() {
  override fun areItemsTheSame(old: T, new: T): Boolean = itemsSame(old, new)
  override fun areContentsTheSame(old: T, new: T): Boolean = contentsSame(old, new)
}) {

  abstract fun onCreateViewHolder(
    parent: ViewGroup,
    inflater: LayoutInflater,
    viewType: Int
  ): RecyclerView.ViewHolder

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
    onCreateViewHolder(
      parent = parent,
      inflater = LayoutInflater.from(parent.context),
      viewType = viewType
    )
}