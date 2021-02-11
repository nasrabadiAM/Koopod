package com.nasabadiam.koopod.ui.search

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.nasabadiam.koopod.R
import com.nasabadiam.koopod.ResourceState
import com.nasabadiam.koopod.podcast.podcastlist.DuplicatePodcastException
import com.nasabadiam.koopod.podcast.podcastlist.PodcastModel
import com.nasabadiam.koopod.podcast.podcastlist.PodcastRepository
import com.nasabadiam.koopod.podcast.podcastlist.Result
import com.nasabadiam.koopod.ui.MessageHandler
import com.nasabadiam.koopod.utils.BaseViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SearchViewModel @ViewModelInject constructor(
    private val searchRepository: SearchRepository,
    private val podcastRepository: PodcastRepository,
    private val errorMessageHandler: MessageHandler
) : BaseViewModel() {

    private var latestQuery: String? = null

    private val _state = MutableStateFlow<ResourceState>(ResourceState.SuccessEmpty)
    val state: StateFlow<ResourceState> = _state

    private val _data = MutableStateFlow<List<SearchPodcastItem>>(listOf())
    val data: StateFlow<List<SearchPodcastItem>> = _data

    private val _notifyItem = MutableSharedFlow<Pair<Int, Any?>>()
    val notifyItem: SharedFlow<Pair<Int, Any?>> = _notifyItem

    private val _navigation = MutableSharedFlow<NavDirections?>()
    val navigation: SharedFlow<NavDirections?> = _navigation

    private val _keyboardEvent = MutableSharedFlow<KeyboardEvent>()
    val keyboardEvent: SharedFlow<KeyboardEvent> = _keyboardEvent

    private val _message = MutableStateFlow<Int?>(null)
    val message: StateFlow<Int?> = _message

    private val _popupMessage = MutableSharedFlow<Int?>()
    val popupMessage: SharedFlow<Int?> = _popupMessage

    init {
        viewModelScope.launch {
            _keyboardEvent.emit(KeyboardEvent.OPEN)
        }
    }

    fun search(query: String?) {
        viewModelScope.launch {
            _keyboardEvent.emit(KeyboardEvent.CLOSE)

            when {
                query.isNullOrBlank() -> {
                    _state.emit(ResourceState.SuccessEmpty)
                    _message.emit(R.string.empty_search_query)
                }
                query == latestQuery && state.value != ResourceState.Failed -> {
                    return@launch
                }
                else -> {
                    _state.emit(ResourceState.Loading)
                    searchRepository.search(query).collect {
                        handleResult(it)
                    }
                }
            }
        }
        latestQuery = query
    }

    private suspend fun handleResult(it: Result<List<PodcastModel>>) {
        when (it) {
            is Result.Success -> {
                if (it.data.isEmpty()) {
                    _state.emit(ResourceState.SuccessEmpty)
                    _message.emit(R.string.empty_search_message)
                } else {
                    _state.emit(ResourceState.Success)
                    _data.emit(it.data.map { model -> SearchPodcastItem.fromModel(model) })
                }
            }
            is Result.Error -> {
                _state.emit(ResourceState.Failed)
                _message.emit(errorMessageHandler.handle(it.error))
            }
        }
    }

    fun onPodcastItemClicked(item: SearchPodcastItem) {
        // TODO("Not yet implemented")
    }

    fun onSubscribePodcastClicked(item: SearchPodcastItem) {
        viewModelScope.launch {
            val index = _data.value.indexOf(item)
            _data.value[index].isLoading = true
            _notifyItem.emit(
                index to SearchPodcastItem.SubscribePayLoad(
                    isLoading = true,
                    isSubscribed = false
                )
            )

            podcastRepository.subscribeToPodcast(item.toPodcastModel()).collect { result ->
                when (result) {
                    is Result.Success -> {
                        _data.value[index].isLoading = false
                        _data.value[index].isSubscribed = true
                        _notifyItem.emit(
                            index to SearchPodcastItem.SubscribePayLoad(
                                isLoading = false,
                                isSubscribed = true
                            )
                        )
                        _popupMessage.emit(R.string.podcast_successfully_added)
                    }
                    is Result.Error -> {
                        if (result.error.throwable is DuplicatePodcastException) {
                            _data.value[index].isLoading = false
                            _data.value[index].isSubscribed = true
                            _notifyItem.emit(
                                index to SearchPodcastItem.SubscribePayLoad(
                                    isLoading = false,
                                    isSubscribed = true
                                )
                            )
                        } else {
                            _data.value[index].isLoading = false
                            _data.value[index].isSubscribed = false
                            _notifyItem.emit(
                                index to SearchPodcastItem.SubscribePayLoad(
                                    isLoading = false,
                                    isSubscribed = false
                                )
                            )
                        }
                        _popupMessage.emit(errorMessageHandler.handle(result.error))
                    }
                }

            }
        }
    }
}

data class SearchPodcastItem(
    val rssLink: String,
    val title: String,
    val image: String,
    val author: String,
    val description: String
) {

    var isLoading: Boolean = false
    var isSubscribed: Boolean = false

    fun canSubscribe(): Boolean {
        return !isLoading && !isSubscribed
    }

    fun toPodcastModel() = PodcastModel(
        rssLink = rssLink,
        title = title,
        image = image,
        author = author,
        description = description
    )

    companion object {
        fun fromModel(model: PodcastModel): SearchPodcastItem = with(model) {
            SearchPodcastItem(
                rssLink = rssLink,
                title = title,
                image = image,
                author = author,
                description = description
            ).apply {
                isSubscribed = model.isSubscribed
            }
        }
    }

    data class SubscribePayLoad(val isLoading: Boolean, val isSubscribed: Boolean)
}

enum class KeyboardEvent {
    OPEN, CLOSE
}