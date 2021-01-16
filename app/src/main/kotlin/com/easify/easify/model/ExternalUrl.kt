package com.easify.easify.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author: deniz.demirci
 * @date: 9/26/2020
 */

@Parcelize
data class ExternalUrl(
  val spotify: String
): Parcelable