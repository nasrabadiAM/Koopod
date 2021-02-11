package com.nasabadiam.koopod.ui.podcastdetail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.nasabadiam.koopod.R
import com.nasabadiam.koopod.ResourceState
import com.nasabadiam.koopod.podcast.podcastlist.EpisodeModel
import com.nasabadiam.koopod.podcast.podcastlist.PodcastModel
import com.nasabadiam.koopod.podcast.podcastlist.PodcastRepository
import com.nasabadiam.koopod.podcast.podcastlist.Result
import com.nasabadiam.koopod.ui.MessageHandler
import com.nasabadiam.koopod.utils.BaseViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PodcastDetailViewModel @ViewModelInject constructor(
    private val podcastRepository: PodcastRepository,
    private val messageHandler: MessageHandler
) : BaseViewModel() {

    private val _state = MutableStateFlow<ResourceState>(ResourceState.Loading)
    val state: StateFlow<ResourceState> = _state

    private val _data = MutableStateFlow<List<EpisodeItem>>(listOf())
    val data: StateFlow<List<EpisodeItem>> = _data

    private val _podcastData = MutableStateFlow<PodcastModel?>(null)
    val podcastData: StateFlow<PodcastModel?> = _podcastData

    private val _navigation = MutableSharedFlow<NavDirections?>()
    val navigation: SharedFlow<NavDirections?> = _navigation

    private val _message = MutableStateFlow<Int?>(null)
    val message: StateFlow<Int?> = _message

    fun onViewCreated(rssLink: String) {
        viewModelScope.launch {
            podcastRepository.podcast(rssLink).collect {
                when (it) {
                    is Result.Success -> {
                        _podcastData.emit(it.data)
                        with(it.data.episodes) {
                            if (isEmpty()) {
                                _state.emit(ResourceState.SuccessEmpty)
                                _message.emit(R.string.empty_episodes)
                            } else {
                                _state.emit(ResourceState.Success)
                                _data.emit(map { item -> EpisodeItem.fromModel(item) })
                            }
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

    fun onEpisodeItemClicked(item: EpisodeItem) {
        viewModelScope.launch {
            _navigation.emit(
                PodcastDetailFragmentDirections.toPlayer(
                    item.episodeId,
                    item.downloadUrl
                )
            )
        }
    }
}

data class EpisodeItem(
    val id: Long,
    val podcastId: Long,
    val guid: String,
    val title: String,
    val image: String,
    val author: String,
    val downloadUrl: String,
    val streamUrl: String,
    val description: String,
    val episodeId: Int
) {

    fun getEpisodeNum(): String = episodeId.toString()

    fun toPodcastModel() = EpisodeModel(
        id = id,
        guid = guid,
        title = title,
        image = image,
        author = author,
        episodeId = episodeId,
        downloadUrl = downloadUrl,
        description = description,
        streamUrl = streamUrl,
        podcastId = podcastId
    )

    companion object {
        fun fromModel(model: EpisodeModel): EpisodeItem = with(model) {
            EpisodeItem(
                id = id,
                podcastId = podcastId,
                guid = guid,
                title = title,
                author = author,
                image = image,
                description = description,
                downloadUrl = downloadUrl,
                streamUrl = streamUrl,
                episodeId = episodeId
            )
        }
    }
}