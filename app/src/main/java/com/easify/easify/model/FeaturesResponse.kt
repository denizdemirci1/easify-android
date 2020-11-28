package com.easify.easify.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author: deniz.demirci
 * @date: 28.11.2020
 */

@Parcelize
data class FeaturesResponse(
  val danceability: Float,
  val energy: Float,
  val speechiness: Float,
  val acousticness: Float,
  val instrumentalness: Float,
  val liveness: Float,
  val valence: Float,
  val tempo: Float,
  val id: String
) : Parcelable
