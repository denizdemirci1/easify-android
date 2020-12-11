package com.easify.easify.model.util

import android.os.Parcelable
import com.easify.easify.model.Image
import kotlinx.android.parcel.Parcelize

/**
 * @author: deniz.demirci
 * @date: 11.12.2020
 */

/**
 * Whether the item we show on screen is track, artist or playlist
 * we only need some specific data of the item and those are common
 *
 * @see [EasifyTrack.id] [EasifyTrack.name] [EasifyTrack.images] etc.
 *
 * We will use a list of EasifyItem to pass to the adapter, so that we
 * don't have to write adapter for each screen.
 */

@Parcelize
data class EasifyItem(
  val type: EasifyItemType,
  val track: EasifyTrack? = null,
  val artist: EasifyArtist? = null,
  val playlist: EasifyPlaylist? = null,
): Parcelable

@Parcelize
data class EasifyTrack(
  val id: String,
  val name: String,
  val artistName: String,
  val images: List<Image>?,
  val uri: String
): Parcelable

@Parcelize
data class EasifyArtist(
  val id: String,
  val name: String,
  val follower: Int,
  val popularity: Int,
  val images: List<Image>?,
  val uri: String
): Parcelable

@Parcelize
data class EasifyPlaylist(
  val id: String,
  val name: String,
  val images: List<Image>?,
  val icon: Icon? = null,
  val isListenable: Boolean = true,
  val ownerId: String,
  val uri: String
): Parcelable

/**
 * To show a custom image for playlist when we don't have
 * a list of images for a playlist, such as Liked Songs
 */
@Parcelize
data class Icon(
  val background: Int,
  val foreground: Int
): Parcelable

enum class EasifyItemType {
  TRACK, ARTIST, PLAYLIST
}
