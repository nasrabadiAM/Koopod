package com.nasabadiam.koopod.ui.player

import com.nasabadiam.koopod.ui.podcastdetail.EpisodeItem

/**
 * base player  state class.
 * any other state for player should implement this class
 */
sealed class PlayerState(val player: Player) {

    /**
     * initialize action
     */
    open fun initialize(): Unit =
        throw UnsupportedOperationException("Operation not supported")

    /**
     * play action
     */
    open suspend fun playOrPause(episodeItem: EpisodeItem?): Unit =
        throw UnsupportedOperationException("Operation not supported")

    /**
     * release action
     */
    open suspend fun release(): Unit =
        throw UnsupportedOperationException("Operation not supported")
}

class NotInitialized(player: Player) : PlayerState(player) {

    override fun initialize() {
        // initialize the player
        // TODO
        player.state = player.initializedState
    }

    override suspend fun playOrPause(episodeItem: EpisodeItem?) {
        // first initialize the player
        initialize()
        // then play track
        // TODO
        player.currentTrack = episodeItem
        player.playerViewState.emit(episodeItem)

        player.notifyItemPlayed.emit(episodeItem)
        player.state = player.playingState
    }

    override suspend fun release() {
        // player is not initialized yet, do nothing
    }
}

class Initialized(player: Player) : PlayerState(player) {

    override fun initialize() {
        // player is already initialized, do nothing
    }

    override suspend fun playOrPause(episodeItem: EpisodeItem?) {
        // play
        // TODO: play first track after initialization
        player.currentTrack = episodeItem
        player.playerViewState.emit(episodeItem)
        player.notifyItemPlayed.emit(episodeItem)
        player.state = player.playingState
    }

    override suspend fun release() {
        // release player stuff
        // TODO
        player.state = player.notInitializedState
    }
}

class Playing(player: Player) : PlayerState(player) {

    override fun initialize() {
        // player is already initialized, do nothing
    }

    override suspend fun playOrPause(episodeItem: EpisodeItem?) {
        // if a new track, pause current and then start new one, else do nothing
        if (player.currentTrack == episodeItem) {
            pause()
            // TODO: pause
        } else {
            pause()
            // TODO: play new one
            player.currentTrack = episodeItem
            player.playerViewState.emit(episodeItem)
            player.notifyItemPlayed.emit(episodeItem)
            player.state = player.playingState
        }
    }

    private suspend fun pause(episodeItem: EpisodeItem? = player.currentTrack) {
        // pause the current track
        // TODO: pause
        player.state = player.pausedState
        player.notifyItemPaused.emit(episodeItem)
        player.playerViewState.emit(episodeItem)
    }

    override suspend fun release() {
        // first pause the current playing track
        pause()
        // then release player stuff
        player.currentTrack = null
        player.playerViewState.emit(null)
        // TODO
        player.state = player.notInitializedState
    }
}

class Paused(player: Player) : PlayerState(player) {

    override fun initialize() {
        // player is already initialized, do nothing
    }

    override suspend fun playOrPause(episodeItem: EpisodeItem?) {
        // if a new track, start new one, else resume current track
        if (player.currentTrack == episodeItem) {
            // TODO: resume
        } else {
            // TODO: play new one
            player.currentTrack = episodeItem
            player.playerViewState.emit(episodeItem)
        }
        player.notifyItemPlayed.emit(episodeItem)
        player.state = player.playingState
    }

    override suspend fun release() {
        // release player stuff
        player.currentTrack = null
        player.playerViewState.emit(null)
        // TODO
        player.state = player.notInitializedState
    }
}