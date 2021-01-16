package com.easify.easify.ui.extensions

import com.easify.easify.model.Playlist
import com.easify.easify.model.util.EasifyItem
import com.easify.easify.model.util.EasifyItemType
import com.easify.easify.model.util.EasifyPlaylist

/**
 * @author: deniz.demirci
 * @date: 11.12.2020
 */

fun List<Playlist>.toEasifyItemList(): ArrayList<EasifyItem> {
  val easifyItems = arrayListOf<EasifyItem>()
  this.forEach { playlist ->
    easifyItems.add(
      EasifyItem(
        type = EasifyItemType.PLAYLIST,
        playlist = playlist.toEasifyPlaylist()
      )
    )
  }
  return easifyItems
}

fun Playlist.toEasifyPlaylist(): EasifyPlaylist {
  return EasifyPlaylist(
    id = id,
    name = name,
    images = images,
    ownerId = owner.id,
    uri = uri
  )
}
