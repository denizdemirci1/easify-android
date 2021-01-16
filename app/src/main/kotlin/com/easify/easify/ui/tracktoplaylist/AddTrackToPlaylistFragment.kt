package com.easify.easify.ui.tracktoplaylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.navArgs
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.afollestad.materialdialogs.MaterialDialog
import com.easify.easify.R
import com.easify.easify.databinding.FragmentAddTrackToPlaylistBinding
import com.easify.easify.model.util.EasifyItem
import com.easify.easify.model.util.EasifyItemType
import com.easify.easify.model.util.EasifyPlaylist
import com.easify.easify.model.util.Icon
import com.easify.easify.ui.common.adapter.EasifyItemPagedListAdapter
import com.easify.easify.ui.tracktoplaylist.data.AddTrackToPlaylistDataSource
import com.easify.easify.util.EventObserver
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_add_track_to_playlist.*

/**
 * @author: deniz.demirci
 * @date: 2.11.2020
 */

@AndroidEntryPoint
class AddTrackToPlaylistFragment : BottomSheetDialogFragment() {

  private val addTrackToPlaylistViewModel by viewModels<AddTrackToPlaylistViewModel>()

  private lateinit var binding: FragmentAddTrackToPlaylistBinding

  private val args: AddTrackToPlaylistFragmentArgs by navArgs()

  private lateinit var easifyItemPagedListAdapter: EasifyItemPagedListAdapter

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_add_track_to_playlist, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    DataBindingUtil.bind<FragmentAddTrackToPlaylistBinding>(add_track_to_playlist_root)?.apply {
      lifecycleOwner = this@AddTrackToPlaylistFragment.viewLifecycleOwner
      addTrackToPlaylistViewModel = this@AddTrackToPlaylistFragment.addTrackToPlaylistViewModel
      binding = this
    }

    args.track?.let { track ->
      addTrackToPlaylistViewModel.setTrackToAdd(track)
    } ?: run { dismiss() }

    setObservers()
    setupAdapter()
  }

  private fun setObservers() {
    addTrackToPlaylistViewModel.event.observe(viewLifecycleOwner, EventObserver{ event ->
      when (event) {
        is AddTrackToPlaylistViewEvent.AlreadyExists -> {
          showSnackBar(event.trackName, event.playlistName, false)
        }
        is AddTrackToPlaylistViewEvent.TrackAdded -> {
          showSnackBar(event.trackName, event.playlistName, true)
        }
        is AddTrackToPlaylistViewEvent.SaveTrack -> {
          showSnackBar(
            event.trackName,
            getString(R.string.fragment_add_track_to_playlist_liked_songs),
            event.isExist.not()
          )
        }
        is AddTrackToPlaylistViewEvent.ShowError -> showError(event.message)
      }
    })

    buildPagedListLiveData().observe(viewLifecycleOwner, { list ->
      easifyItemPagedListAdapter.submitList(list)
    })
  }

  private fun buildPagedListLiveData(): LiveData<PagedList<EasifyItem>> {
    return LivePagedListBuilder(
      object : DataSource.Factory<String, EasifyItem>() {
        override fun create(): DataSource<String, EasifyItem> {
          return AddTrackToPlaylistDataSource(addTrackToPlaylistViewModel, getInitialPlaylists())
        }
      }, 30).build()
  }

  private fun getInitialPlaylists(): ArrayList<EasifyItem> {
    return arrayListOf(
      EasifyItem(
        type = EasifyItemType.PLAYLIST,
        playlist = EasifyPlaylist(
          id = "",
          name = getString(R.string.fragment_add_track_to_playlist_liked_songs),
          images = null,
          icon = Icon(background = R.drawable.bg_liked_songs, R.drawable.ic_favorite_fill),
          isListenable = false,
          ownerId = "",
          uri = ""
        )
      )
    )
  }

  private fun setupAdapter() {
    easifyItemPagedListAdapter = EasifyItemPagedListAdapter(addTrackToPlaylistViewModel)
    binding.playlistsRecyclerView.adapter = easifyItemPagedListAdapter
  }

  private fun showSnackBar(
    trackName: String,
    playlistName: String,
    isAdded: Boolean
  ) {
    val message = getSnackBarMessage(trackName, playlistName, isAdded)
    view?.let {
      val snackbar = Snackbar.make(it, message, Snackbar.LENGTH_LONG)
      context?.let { safeContext ->
        snackbar.view.background = ContextCompat.getDrawable(
          safeContext,
          if (isAdded) R.drawable.bg_snackbar_success else R.drawable.bg_snackbar_fail
        )
      }
      val params = snackbar.view.layoutParams as ViewGroup.MarginLayoutParams
      params.setMargins(24, 24, 24, 72)
      snackbar.view.layoutParams = params
      snackbar.show()
    }
  }

  private fun getSnackBarMessage(
    trackName: String,
    playlistName: String,
    isAdded: Boolean
  ) = getString(
    if (isAdded) {
      R.string.fragment_add_track_to_playlist_track_added_to_playlist
    } else {
      R.string.fragment_add_track_to_playlist_track_exists_in_playlist
    },
    trackName,
    playlistName
  )

  private fun showError(message: String?) {
    MaterialDialog(requireContext()).show {
      title(R.string.dialog_error_title)
      message(text = message ?: getString(R.string.dialog_common_error_text))
      positiveButton(R.string.dialog_ok)
    }
  }
}
