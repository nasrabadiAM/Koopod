package com.nasabadiam.koopod.ui.podcastlist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.nasabadiam.koopod.R
import com.nasabadiam.koopod.ResourceState
import com.nasabadiam.koopod.podcast.podcastlist.PodcastModel
import com.nasabadiam.koopod.podcast.podcastlist.PodcastRepository
import com.nasabadiam.koopod.podcast.podcastlist.Result
import com.nasabadiam.koopod.ui.MessageHandler
import com.nasabadiam.koopod.utils.BaseViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PodcastListViewModel @ViewModelInject constructor(
    private val podcastRepository: PodcastRepository,
    private val messageHandler: MessageHandler
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
            podcastRepository.getPodcasts().collect {
                when (it) {
                    is Result.Success -> {
                        if (it.data.isEmpty()) {
                            _state.emit(ResourceState.SuccessEmpty)
                            _message.emit(R.string.empty_message)
                        } else {
                            _state.emit(ResourceState.Success)
                            _data.emit(it.data.map { item -> PodcastItem.fromModel(item) })
                        }
                    }
                    is Result.Error -> {
                        _state.emit(ResourceState.Failed)
                        _message.emit(messageHandler.handle(it.error))
                    }
                }

            }
        }
    }

    fun onPodcastItemClicked(item: PodcastItem) {
        viewModelScope.launch {
            _navigation.emit(PodcastListFragmentDirections.toPodCastDetail(item.rssLink))
        }
    }

    fun onSearchBarClicked() {
        viewModelScope.launch {
            _navigation.emit(PodcastListFragmentDirections.toSearch())
        }
    }

}

data class PodcastItem(
    val rssLink: String,
    val title: String,
    val image: String,
    val author: String,
    val description: String
) {

    fun toPodcastModel() = PodcastModel(
        rssLink = rssLink,
        title = title,
        image = image,
        author = author,
        description = description
    )

    companion object {
        fun fromModel(model: PodcastModel): PodcastItem = with(model) {
            PodcastItem(
                rssLink = rssLink,
                title = title,
                image = image,
                author = author,
                description = description
            )
        }
    }
}