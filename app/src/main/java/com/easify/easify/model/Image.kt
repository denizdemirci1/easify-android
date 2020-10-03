package com.easify.easify.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author: deniz.demirci
 * @date: 9/5/2020
 */

@Parcelize
data class Image(
    val height: Int?,
    val url: String,
    val width: Int?
): Parcelable
