package com.nasabadiam.koopod.ui.podcastlist

import com.nasabadiam.koopod.ResourceState
import com.nasabadiam.koopod.utils.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PodcastListViewModel : BaseViewModel() {

    private val _state = MutableStateFlow<ResourceState>(ResourceState.Loading)
    val state: StateFlow<ResourceState> = _state
    private val _data =
        MutableStateFlow<PodcastListItem>(PodcastListItem(listOf()))
    val data: StateFlow<PodcastListItem> = _data

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    init {
        _state.value = ResourceState.Loading
    }
}

data class PodcastListItem(val podcasts: List<PodcastItem>)

data class PodcastItem(val name: String)