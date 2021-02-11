package com.nasabadiam.koopod.ui.player

import com.nasabadiam.koopod.ui.podcastdetail.EpisodeItem
import kotlinx.coroutines.flow.MutableSharedFlow

class Player {
    val notInitializedState = NotInitialized(this)
    val initializedState = Initialized(this)
    val playingState = Playing(this)
    val pausedState = Paused(this)

    var state: PlayerState = notInitializedState

    var currentTrack: EpisodeItem? = null

    val notifyItemPaused = MutableSharedFlow<EpisodeItem?>()
    val playerViewState = MutableSharedFlow<EpisodeItem?>()
    val notifyItemPlayed = MutableSharedFlow<EpisodeItem?>()

    init {
        state.initialize()
    }

    suspend fun playOrPause(episodeItem: EpisodeItem?) {
        state.playOrPause(episodeItem)
    }

    suspend fun release() {
        playerViewState.emit(null)
        state.release()
    }
}