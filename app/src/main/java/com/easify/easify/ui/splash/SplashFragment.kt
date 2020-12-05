package com.easify.easify.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.easify.easify.BuildConfig
import com.easify.easify.R
import com.easify.easify.databinding.FragmentSplashBinding
import com.easify.easify.ui.base.BaseFragment
import com.easify.easify.util.EventObserver
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
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
        SplashViewEvent.Authenticate -> openSpotifyLoginActivity()

        SplashViewEvent.OpenHomePage -> findNavController().navigate(R.id.homeFragment)

        is SplashViewEvent.ShowError -> showNetworkError(event.message)
      }
    })
  }

  private fun openSpotifyLoginActivity() {
    val builder = AuthenticationRequest.Builder(
      BuildConfig.SPOTIFY_CLIENT_ID,
      AuthenticationResponse.Type.TOKEN,
      getString(R.string.spotify_uri_callback)
    )

    builder.setScopes(arrayOf(SCOPES))

    AuthenticationClient.openLoginActivity(
      requireActivity(),
      SPOTIFY_REQUEST_CODE,
      builder.build()
    )
  }

  private fun showNetworkError(message: String) {
    requireActivity().let {
      MaterialDialog(it).show {
        title(R.string.dialog_error_title)
        message(text = message)
        positiveButton(R.string.dialog_positive_button_text) {
          viewModel.authenticateSpotify()
        }
        negativeButton(R.string.dialog_negative_button_text) { _ ->
          it.finish()
        }
      }
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    // Check if result comes from the correct activity
    if (requestCode == SPOTIFY_REQUEST_CODE) {
      val response = AuthenticationClient.getResponse(resultCode, data)

      when (response.type) {
        // Response was successful and contains auth token
        AuthenticationResponse.Type.TOKEN -> {
          viewModel.saveToken(response.accessToken)
          viewModel.fetchUser()
        }

        // Auth flow returned an error
        AuthenticationResponse.Type.ERROR -> {
          viewModel.clearToken()
          viewModel.handleAuthError(response.error)
        }

        else -> showNetworkError(getString(R.string.dialog_common_error_text))
      }
    }
  }

  companion object {
    private const val SPOTIFY_REQUEST_CODE = 1337
    private const val SCOPES = "user-read-recently-played," +
            "user-read-currently-playing," +
            "user-library-modify," +
            "user-library-read," +
            "user-read-email," +
            "user-read-private," +
            "user-top-read," +
            "user-follow-read," +
            "user-follow-modify," +
            "user-modify-playback-state," +
            "user-read-playback-state," +
            "playlist-read-private," +
            "playlist-read-collaborative," +
            "playlist-modify-public," +
            "playlist-modify-private"
  }
}
