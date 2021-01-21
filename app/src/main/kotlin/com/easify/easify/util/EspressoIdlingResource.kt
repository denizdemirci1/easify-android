package com.easify.easify.util

import androidx.test.espresso.idling.CountingIdlingResource

/**
 * @author: deniz.demirci
 * @date: 17.01.2021
 */

object EspressoIdlingResource {

  private const val RESOURCE = "GLOBAL"

  @JvmField
  val countingIdlingResource = CountingIdlingResource(RESOURCE)

  fun increment() {
    countingIdlingResource.increment()
  }

  fun decrement() {
    if (!countingIdlingResource.isIdleNow) {
      countingIdlingResource.decrement()
    }
  }
}

inline fun <T> wrapEspressoIdlingResource(function: () -> T): T {
  // Espresso does not work well with coroutines yet. See
  // https://github.com/Kotlin/kotlinx.coroutines/issues/982
  EspressoIdlingResource.increment() // Set app as busy.
  return try {
    function()
  } finally {
    EspressoIdlingResource.decrement() // Set app as idle.
  }
}
