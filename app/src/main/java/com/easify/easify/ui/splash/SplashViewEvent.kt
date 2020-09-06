package com.easify.easify.ui.splash

/**
 * @author: deniz.demirci
 * @date: 9/5/2020
 */

sealed class SplashViewEvent {

  object Authenticate : SplashViewEvent()

  object OpenHomePage : SplashViewEvent()

  data class ShowError(val message: String) : SplashViewEvent()
}