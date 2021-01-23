package com.easify.easify.ui.extensions

import androidx.fragment.app.FragmentActivity
import com.google.android.play.core.review.ReviewManagerFactory
import androidx.lifecycle.lifecycleScope
import com.easify.easify.util.MONTH
import com.easify.easify.util.manager.UserManager
import kotlinx.coroutines.launch

/**
 * @author: deniz.demirci
 * @date: 23.01.2021
 */

fun FragmentActivity?.requestInAppReviewDialog(
  userManager: UserManager
) {
  this?.let { activity ->
    activity.lifecycleScope.launch {

      val lastShownTime = userManager.inAppReviewLastShownTime
      val today = System.currentTimeMillis()

      if (today - lastShownTime > MONTH) {
        userManager.inAppReviewLastShownTime = today

        val manager = ReviewManagerFactory.create(activity)

        manager.requestReviewFlow().addOnCompleteListener { request ->
          if (request.isSuccessful) {
            manager.launchReviewFlow(activity, request.result)
          }
        }
      }
    }
  }
}