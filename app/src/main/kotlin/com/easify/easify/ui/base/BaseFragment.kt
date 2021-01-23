package com.easify.easify.ui.base

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.adcolony.sdk.AdColony
import com.adcolony.sdk.AdColonyAdSize
import com.adcolony.sdk.AdColonyAdView
import com.adcolony.sdk.AdColonyAdViewListener
import com.afollestad.materialdialogs.MaterialDialog
import com.easify.easify.BuildConfig
import com.easify.easify.R
import com.easify.easify.ui.splash.SplashFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse

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

/**
 * @author: deniz.demirci
 * @date: 9/4/2020
 */

open class BaseFragment : Fragment {

  constructor() : super()
  constructor(@LayoutRes resId: Int) : super(resId)

  private var afterLogin: (
    (responseType: AuthenticationResponse.Type?,
     response: String) -> Unit
  )? = null

  internal fun showBottomNavigation(show: Boolean) {
    if (show) {
      activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation_view)?.visibility =
        View.VISIBLE
    } else {
      activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation_view)?.visibility =
        View.GONE
    }
  }

  fun requestAds(view: ConstraintLayout) {
    val listener: AdColonyAdViewListener = object : AdColonyAdViewListener() {
      override fun onRequestFilled(ad: AdColonyAdView) {
        view.addView(ad)
      }
    }

    AdColony.requestAdView(BuildConfig.ADCOLONY_BANNER_AD_ZONE_ID_HOME, listener, AdColonyAdSize.BANNER)
  }

  fun openSpotifyLoginActivity(
    onLogin: ((responseType: AuthenticationResponse.Type?, response: String) -> Unit)? = null
  ) {
    afterLogin = onLogin
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

  fun showError(message: String?) {
    MaterialDialog(requireContext()).show {
      title(R.string.dialog_error_title)
      message(text = message ?: getString(R.string.dialog_common_error_text))
      positiveButton(R.string.dialog_ok)
    }
  }

  fun showOpenSpotifyWarning() {
    MaterialDialog(requireContext()).show {
      title(R.string.dialog_error_title)
      message(R.string.dialog_should_open_spotify)
      positiveButton(R.string.dialog_ok)
    }
  }

  fun openUriWithSpotify(uri: String) {
    if (isSpotifyInstalled()) {
      startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
    } else {
      showOpenSpotifyWarning()
    }
  }

  private fun isSpotifyInstalled(): Boolean {
    val pm = requireActivity().packageManager
    return try {
      pm.getPackageInfo("com.spotify.music", 0)
      true
    } catch (e: PackageManager.NameNotFoundException) {
      false
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    // Check if result comes from the correct activity
    if (requestCode == SPOTIFY_REQUEST_CODE) {
      val response = AuthenticationClient.getResponse(resultCode, data)

      when (response.type) {
        AuthenticationResponse.Type.TOKEN ->
          afterLogin?.invoke(AuthenticationResponse.Type.TOKEN, response.accessToken)

        AuthenticationResponse.Type.ERROR ->
          afterLogin?.invoke(AuthenticationResponse.Type.ERROR, response.error)

        else -> afterLogin?.invoke(null, "")
      }
    }
  }
}
