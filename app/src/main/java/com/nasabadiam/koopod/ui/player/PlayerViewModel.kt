package com.nasabadiam.koopod.ui.player

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.nasabadiam.koopod.ui.podcastdetail.EpisodeItem
import com.nasabadiam.koopod.utils.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class PlayerViewModel @ViewModelInject constructor() : BaseViewModel() {

    private val _playerState = MutableSharedFlow<Pair<PlayerStates, EpisodeItem?>>()
    val playerState: SharedFlow<Pair<PlayerStates, EpisodeItem?>> = _playerState

    private val _playerAction = MutableSharedFlow<EpisodeItem?>()
    val playerAction: SharedFlow<EpisodeItem?> = _playerAction

    fun onPlayPauseActionClicked(episodeItem: EpisodeItem? = null) {
        viewModelScope.launch {
            _playerAction.emit(episodeItem)
        }
    }

    suspend fun onPlayerDisconnected() {
        _playerState.emit(PlayerStates.NOTHING to null)
    }

    suspend fun onPlayerTrackEnded() {
        _playerState.emit(PlayerStates.NOTHING to null)
    }

    suspend fun onPlayerStateChanged(state: PlayerStates, episodeItem: EpisodeItem?) {
        _playerState.emit(state to episodeItem)
    }
}