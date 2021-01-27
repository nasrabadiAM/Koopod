package com.nasabadiam.koopod.ui.podcastlist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.nasabadiam.koopod.R
import com.nasabadiam.koopod.ResourceState
import com.nasabadiam.koopod.podcast.podcastlist.PodcastLocalDataSource
import com.nasabadiam.koopod.podcast.podcastlist.PodcastModel
import com.nasabadiam.koopod.utils.BaseViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PodcastListViewModel @ViewModelInject constructor(
    private val podcastLocalDataSource: PodcastLocalDataSource
) : BaseViewModel() {

    private val _state = MutableStateFlow<ResourceState>(ResourceState.Loading)
    val state: StateFlow<ResourceState> = _state

    private val _data = MutableStateFlow<List<PodcastItem>>(listOf())
    val data: StateFlow<List<PodcastItem>> = _data

    private val _navigation = MutableSharedFlow<NavDirections?>()
    val navigation: SharedFlow<NavDirections?> = _navigation

    private val _message = MutableStateFlow<Int?>(null)
    val message: StateFlow<Int?> = _message

    fun onViewCreated() {
        viewModelScope.launch {
            podcastLocalDataSource.getPodcasts().collect {
                if (it.isEmpty()) {
                    _state.emit(ResourceState.SuccessEmpty)
                    _message.emit(R.string.empty_message)
                } else {
                    _state.emit(ResourceState.Success)
                    _data.emit(it.map { item -> PodcastItem.fromModel(item) })
                }
            }
        }
    }

    fun onPodcastItemClicked(item: PodcastItem) {
        viewModelScope.launch {
            _navigation.emit(PodcastListFragmentDirections.toPodCastDetail(item.id))
        }
    }

    fun onSearchBarClicked() {
        viewModelScope.launch {
            _navigation.emit(PodcastListFragmentDirections.toSearch())
        }
    }

}

data class PodcastItem(
    val id: String,
    val name: String,
    val image: String,
    val description: String
) {
    companion object {
        fun fromModel(model: PodcastModel): PodcastItem = with(model) {
            PodcastItem(id, name, image, description)
        }
    }
}