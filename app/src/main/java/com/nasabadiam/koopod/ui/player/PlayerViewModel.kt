package com.nasabadiam.koopod.ui.player

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.nasabadiam.koopod.ui.podcastdetail.EpisodeItem
import com.nasabadiam.koopod.utils.BaseViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class PlayerViewModel @ViewModelInject constructor(private val player: Player) : BaseViewModel() {

    val notifyItemPaused: SharedFlow<EpisodeItem?> = player.notifyItemPaused
    val notifyItemPlayed: SharedFlow<EpisodeItem?> = player.notifyItemPlayed
    val playerViewState: SharedFlow<EpisodeItem?> = player.playerViewState

    fun onPlayPauseAction(episodeItem: EpisodeItem? = player.currentTrack) {
        viewModelScope.launch {
            player.playOrPause(episodeItem)
        }
    }
}