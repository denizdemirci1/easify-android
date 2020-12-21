package com.easify.easify.ui.splash

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.easify.easify.R
import com.easify.easify.databinding.FragmentSplashBinding
import com.easify.easify.ui.base.BaseFragment
import com.easify.easify.util.EventObserver
import com.spotify.sdk.android.authentication.AuthenticationResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_splash.*

/**
 * @author: deniz.demirci
 * @date: 9/4/2020
 */

@AndroidEntryPoint
class SplashFragment : BaseFragment(R.layout.fragment_splash) {

  private val viewModel by viewModels<SplashViewModel>()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    DataBindingUtil.bind<FragmentSplashBinding>(splashRoot)?.apply {
      lifecycleOwner = this@SplashFragment.viewLifecycleOwner
      viewModel = this@SplashFragment.viewModel
    }
    showBottomNavigation(false)
    viewModel.authenticateSpotify()
    setObservers()
  }

  private fun setObservers() {
    viewModel.event.observe(viewLifecycleOwner, EventObserver { event ->
      when (event) {
        SplashViewEvent.Authenticate -> openSpotifyLoginActivity(::afterLogin)

        SplashViewEvent.OpenHomePage -> findNavController().navigate(R.id.homeFragment)

        is SplashViewEvent.ShowError -> showNetworkError(event.message)
      }
    })
  }

  private fun afterLogin(
    responseType: AuthenticationResponse.Type?,
    response: String
  ) {
    when (responseType) {
      AuthenticationResponse.Type.TOKEN -> {
        viewModel.saveToken(response)
        viewModel.fetchUser()
      }
      AuthenticationResponse.Type.ERROR -> {
        viewModel.clearToken()
        viewModel.handleAuthError(response)
      }
      else -> showNetworkError(getString(R.string.dialog_common_error_text))
    }
  }

  private fun showNetworkError(message: String?) {
    requireActivity().let {
      MaterialDialog(it).show {
        title(R.string.dialog_error_title)
        message(text = message ?: getString(R.string.dialog_common_error_text))
        positiveButton(R.string.dialog_positive_button_text) {
          viewModel.authenticateSpotify()
        }
        negativeButton(R.string.dialog_negative_button_text) { _ ->
          it.finish()
        }
      }
    }
  }
}
