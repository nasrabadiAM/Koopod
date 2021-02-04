package com.nasabadiam.koopod.ui.search

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.nasabadiam.koopod.R
import com.nasabadiam.koopod.ResourceState
import com.nasabadiam.koopod.ui.podcastlist.PodcastItem
import com.nasabadiam.koopod.utils.BaseViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SearchViewModel @ViewModelInject constructor(
    private val searchRemoteDataSource: SearchRemoteDataSource
) : BaseViewModel() {

    private var latestQuery: String? = null

    private val _state = MutableStateFlow<ResourceState>(ResourceState.SuccessEmpty)
    val state: StateFlow<ResourceState> = _state

    private val _data = MutableStateFlow<List<PodcastItem>>(listOf())
    val data: StateFlow<List<PodcastItem>> = _data

    private val _navigation = MutableSharedFlow<NavDirections?>()
    val navigation: SharedFlow<NavDirections?> = _navigation

    private val _keyboardEvent = MutableSharedFlow<KeyboardEvent>()
    val keyboardEvent: SharedFlow<KeyboardEvent> = _keyboardEvent

    private val _message = MutableStateFlow<Int?>(null)
    val message: StateFlow<Int?> = _message

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
                query == latestQuery -> {
                    return@launch
                }
                else -> {
                    _state.emit(ResourceState.Loading)
                    searchRemoteDataSource.search(query).collect {
                        if (it.isEmpty()) {
                            _state.emit(ResourceState.SuccessEmpty)
                            _message.emit(R.string.empty_search_message)
                        } else {
                            _state.emit(ResourceState.Success)
                            _data.emit(it.map { model -> PodcastItem.fromModel(model) })
                        }
                    }
                }
            }
        }
        latestQuery = query
    }

    fun onPodcastItemClicked(item: PodcastItem) {
        // TODO("Not yet implemented")
    }
}

enum class KeyboardEvent {
    OPEN, CLOSE
}
