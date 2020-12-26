package com.easify.easify.ui.extensions

import androidx.fragment.app.FragmentActivity
import com.easify.easify.util.MONTH
import com.easify.easify.util.manager.UserManager
import com.google.android.play.core.review.ReviewManagerFactory

/**
 * @author: deniz.demirci
 * @date: 26.12.2020
 */

fun FragmentActivity?.requestInAppReviewDialog(userManager: UserManager) {
  this?.let { activity ->
    val lastShownTime = userManager.inAppReviewLastShownTime
    val now = System.currentTimeMillis()

    if (now - lastShownTime > MONTH) {
      userManager.inAppReviewLastShownTime = now

      val manager = ReviewManagerFactory.create(activity)

      manager.requestReviewFlow().addOnCompleteListener { request ->
        if (request.isSuccessful) {
          manager.launchReviewFlow(activity, request.result)
        }
      }
    }
  }
}