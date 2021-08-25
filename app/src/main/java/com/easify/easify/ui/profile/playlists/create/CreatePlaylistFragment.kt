package com.easify.easify.ui.profile.playlists.create

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.easify.easify.BuildConfig
import com.easify.easify.R
import com.easify.easify.databinding.FragmentCreatePlaylistBinding
import com.easify.easify.ui.base.BaseFragment
import com.easify.easify.ui.extensions.hideKeyboard
import com.easify.easify.util.EventObserver
import com.spotify.sdk.android.authentication.AuthenticationResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_create_playlist.*

/**
 * @author: deniz.demirci
 * @date: 10/3/2020
 */

@AndroidEntryPoint
class CreatePlaylistFragment : BaseFragment(R.layout.fragment_create_playlist) {

  private val createPlaylistViewModel by viewModels<CreatePlaylistViewModel>()

  private lateinit var binding: FragmentCreatePlaylistBinding

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    DataBindingUtil.bind<FragmentCreatePlaylistBinding>(create_playlist_root)?.apply {
      lifecycleOwner = this@CreatePlaylistFragment.viewLifecycleOwner
      createPlaylistViewModel = this@CreatePlaylistFragment.createPlaylistViewModel
      binding = this
    }
    setupObservers()
    setupListeners()
  }

  private fun setupObservers() {
    createPlaylistViewModel.event.observe(viewLifecycleOwner, EventObserver { event ->
      when (event) {
        CreatePlaylistViewEvent.Authenticate -> openSpotifyLoginActivity(::afterLogin)
        CreatePlaylistViewEvent.Navigate -> findNavController().popBackStack()
        CreatePlaylistViewEvent.ShowUserIdNotFoundError -> {
          showError(getString(R.string.fragment_create_playlist_user_id_not_found_error))
        }
        is CreatePlaylistViewEvent.ShowError -> showError(event.message)
      }
    })
  }

  private fun afterLogin(
    responseType: AuthenticationResponse.Type?,
    response: String
  ) {
    when (responseType) {
      AuthenticationResponse.Type.TOKEN -> {
        createPlaylistViewModel.saveToken(response)
        createPlaylistViewModel.createPlaylist(getName(), getDescription())
      }
      AuthenticationResponse.Type.ERROR -> createPlaylistViewModel.clearToken()
      else -> Unit
    }
  }

  private fun setupListeners() {
    binding.create.setOnClickListener {
      binding.create.isEnabled = false
      it.hideKeyboard()
      createPlaylistViewModel.createPlaylist(getName(), getDescription())
    }
  }

  private fun getName(): String {
    return if (binding.name.text.toString().isEmpty())
      getString(R.string.fragment_create_playlist_default_playlist_name) else
      binding.name.text.toString()
  }

  private fun getDescription(): String {
    return if (binding.description.text.toString().isEmpty())
      getString(R.string.fragment_create_playlist_default_playlist_description) else
      binding.description.text.toString()
  }
}

