package com.easify.easify.ui.home

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easify.easify.data.repositories.SearchRepository
import com.easify.easify.util.manager.UserManager
import kotlinx.coroutines.launch

/**
 * @author: deniz.demirci
 * @date: 9/5/2020
 */

class HomeViewModel @ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    private val searchRepository: SearchRepository,
    private val userManager: UserManager
) : ViewModel() {

    fun search(q: String) {
        viewModelScope.launch {

        }
    }
}
