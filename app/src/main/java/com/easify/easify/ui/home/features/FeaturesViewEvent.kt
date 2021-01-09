package com.easify.easify.ui.home.features

import com.easify.easify.model.FeaturesResponse

/**
 * @author: deniz.demirci
 * @date: 28.11.2020
 */

sealed class FeaturesViewEvent {

  object Authenticate : FeaturesViewEvent()

  data class OnFeaturesReceived(val features: FeaturesResponse): FeaturesViewEvent()

  data class ShowError(val message: String?) : FeaturesViewEvent()
}
