package com.easify.easify.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author: deniz.demirci
 * @date: 9/8/2020
 */

@Parcelize
data class Follower(
    val href: String?,
    val total: Int
): Parcelable
