package com.easify.easify.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author: deniz.demirci
 * @date: 29.11.2020
 */

@Parcelize
data class RecommendationsResponse(
  val tracks: List<Track>
) : Parcelable
