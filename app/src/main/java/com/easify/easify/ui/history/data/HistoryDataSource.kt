package com.easify.easify.ui.history.data

import androidx.paging.PageKeyedDataSource
import com.easify.easify.model.History
import com.easify.easify.model.HistoryResponse
import com.easify.easify.ui.history.HistoryViewModel
import javax.inject.Inject

/**
 * @author: deniz.demirci
 * @date: 9/18/2020
 */

class HistoryDataSource @Inject constructor(
  private val viewModel: HistoryViewModel
) : PageKeyedDataSource<String, History>() {

  private var before: String? = null
  private var uniqueItemsCount = 0
  private val historyToShow = ArrayList<History>()

  override fun loadInitial(
    params: LoadInitialParams<String>,
    callback: LoadInitialCallback<String, History>
  ) {
    viewModel.fetchRecentlyPlayedSongs(null) { data: HistoryResponse ->
      if (data.items.isNotEmpty()) {
        before = data.cursors?.before
        historyToShow.addAll(data.items.distinctBy { it.track.id })
        uniqueItemsCount = historyToShow.size
        callback.onResult(
          historyToShow,
          null,
          if (before != null) data.next else null
        )
      }
    }
  }

  override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, History>) {}

  override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, History>) {
    viewModel.fetchRecentlyPlayedSongs(before) { data: HistoryResponse ->
      before = data.cursors?.before
      val currentUniqueItemList = data.items.distinctBy { it.track.id }
      historyToShow.addAll(currentUniqueItemList)
      val uniqueItemsList = historyToShow.distinctBy { it.track.id }
      callback.onResult(
        if (uniqueItemsCount < uniqueItemsList.size)
          uniqueItemsList.subList(uniqueItemsCount, uniqueItemsList.size) else
          listOf(),
        if (before != null) data.next else null
      )
      uniqueItemsCount += currentUniqueItemList.size
    }
  }
}
