package com.nasabadiam.koopod.ui.player

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavDeepLinkBuilder
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.nasabadiam.koopod.R
import com.nasabadiam.koopod.ui.MainActivity
import com.nasabadiam.koopod.ui.podcastdetail.EpisodeItem
import com.nasabadiam.koopod.utils.ImageLoader
import com.nasabadiam.koopod.utils.ImageLoaderTarget
import com.nasabadiam.koopod.utils.fromHtml
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class PlayerService : Service(), CoroutineScope {

    @Inject
    lateinit var exoPlayer: ExoPlayer

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private var connectionCallback: ConnectionCallback? = null
    private var binder: PlayerBinder = PlayerBinder()

    private var currentPlayingItem: EpisodeItem? = null

    private val exoListener = getPlayerListener()
    private var playerNotificationManager: PlayerNotificationManager? = null
    private val dataSourceFactory: DataSource.Factory by lazy {
        DefaultDataSourceFactory(applicationContext, "Koopod")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        currentPlayingItem = intent?.getSerializableExtra(EPISODE_ITEM_KEY) as EpisodeItem?
        if (currentPlayingItem != null) {
            playNewMedia(requireNotNull(currentPlayingItem))
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    fun setCallBack(callBack: ConnectionCallback) {
        connectionCallback = callBack
        connectionCallback?.onPlayerServiceConnected()
    }

    fun removeCallback() {
        connectionCallback = null
    }

    override fun onDestroy() {
        connectionCallback?.onPlayerServiceDisconnected()
        release()
        super.onDestroy()
    }

    private fun release() {
        playerNotificationManager?.setPlayer(null)
        playerNotificationManager = null
        exoPlayer.removeListener(exoListener)
        exoPlayer.release()
        cancel()
    }

    fun playOrPauseMedia(episodeItem: EpisodeItem?) {
        connectionCallback?.notifyPlayerState(PlayerStates.LOADING, currentPlayingItem)

        if (episodeItem == null) {
            when {
                exoPlayer.isPlaying -> exoPlayer.pause()
                currentPlayingItem != null -> exoPlayer.play()
                else -> exoPlayer.stop()
            }
        } else {
            if (episodeItem == currentPlayingItem) {
                if (exoPlayer.isPlaying) {
                    exoPlayer.pause()
                } else {
                    exoPlayer.play()
                }
            } else {
                exoPlayer.stop()
                exoPlayer.clearMediaItems()
                playNewMedia(episodeItem)
            }
        }
    }

    private fun playNewMedia(episodeItem: EpisodeItem) {

        preparePlayer(requireNotNull(episodeItem.streamUrl))

        exoPlayer.playWhenReady = true
        exoPlayer.addListener(exoListener)

        playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(
            this,
            PLAYBACK_CHANNEL_ID,
            R.string.player_notification_channel_name,
            R.string.player_notification_channel_description,
            PLAYBACK_NOTIFICATION_ID,
            object : PlayerNotificationManager.MediaDescriptionAdapter {
                override fun getCurrentContentTitle(player: Player): CharSequence {
                    return requireNotNull(currentPlayingItem).title
                }

                override fun createCurrentContentIntent(player: Player): PendingIntent {
                    val bundle = Bundle().apply {
                        putString("guid", currentPlayingItem?.guid)
                    }
                    return NavDeepLinkBuilder(this@PlayerService)
                        .setComponentName(MainActivity::class.java)
                        .setGraph(R.navigation.mobile_graph)
                        .setDestination(R.id.podcastDetailFragment)
                        .setArguments(bundle)
                        .createPendingIntent()
                }

                override fun getCurrentContentText(player: Player): CharSequence? {
                    return currentPlayingItem?.description?.fromHtml()
                }

                override fun getCurrentLargeIcon(
                    player: Player,
                    callback: PlayerNotificationManager.BitmapCallback
                ): Bitmap? {
                    ImageLoader.loadImage(
                        this@PlayerService,
                        requireNotNull(currentPlayingItem).image,
                        object : ImageLoaderTarget {
                            override fun onResourceReady(resource: Drawable) {
                                callback.onBitmap(resource.toBitmap())
                            }
                        })
                    return null
                }
            },
            object : PlayerNotificationManager.NotificationListener {
                override fun onNotificationPosted(
                    notificationId: Int,
                    notification: Notification,
                    ongoing: Boolean
                ) {
                    startForeground(notificationId, notification)
                    super.onNotificationPosted(notificationId, notification, ongoing)
                }

                override fun onNotificationCancelled(
                    notificationId: Int,
                    dismissedByUser: Boolean
                ) {
                    stopSelf()
                    super.onNotificationCancelled(notificationId, dismissedByUser)
                }
            }
        ).apply {
            setPlayer(exoPlayer)
        }
        currentPlayingItem = episodeItem
    }


    private fun preparePlayer(videoUrl: String) {
        val uri = Uri.parse(videoUrl)
        val mediaSource = buildMediaSource(uri)
        exoPlayer.setMediaSource(mediaSource)
        exoPlayer.prepare()
    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        val mediaItem = MediaItem.fromUri(uri)
        return ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)
    }

    private fun getPlayerListener() = object : Player.EventListener {
        override fun onPlayerError(error: ExoPlaybackException) {
            connectionCallback?.notifyPlayerError(error, currentPlayingItem)
        }

        override fun onTimelineChanged(timeline: Timeline, reason: Int) {
            super.onTimelineChanged(timeline, reason)
            connectionCallback?.notifyPlayerState(PlayerStates.PLAYING, currentPlayingItem)
        }

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            when (playbackState) {
                Player.STATE_BUFFERING, Player.STATE_READY -> {
                    val state = if (playWhenReady && exoPlayer.isPlaying) {
                        PlayerStates.PLAYING
                    } else {
                        PlayerStates.PAUSED
                    }
                    connectionCallback?.notifyPlayerState(state, currentPlayingItem)
                    if (playWhenReady.not()) {
                        stopForeground(false)
                    }
                }
                Player.STATE_IDLE, Player.STATE_ENDED -> {
                    connectionCallback?.notifyPlayEnded(currentPlayingItem)
                    stopForeground(false)
                }
            }
        }

    }

    fun updateCurrentState() {
        if (currentPlayingItem != null) {
            val state = if (exoPlayer.isPlaying) PlayerStates.PLAYING else PlayerStates.LOADING
            connectionCallback?.notifyPlayerState(state, currentPlayingItem)
        }
    }

    inner class PlayerBinder : Binder() {

        fun getService(): PlayerService {
            return this@PlayerService
        }
    }

    companion object {
        const val EPISODE_ITEM_KEY = "episodeItem"
        private const val PLAYBACK_CHANNEL_ID = "Player"
        private const val PLAYBACK_NOTIFICATION_ID = 123232


        fun bind(context: Context, connection: ServiceConnection, episodeItem: EpisodeItem?) {
            val intent: Intent = getIntent(context, episodeItem)
            context.bindService(
                intent,
                connection,
                Context.BIND_AUTO_CREATE
            )
        }

        fun startService(context: Context, episode: EpisodeItem?) {
            context.startService(getIntent(context, episode))
        }

        fun getIntent(context: Context): Intent {
            return Intent(context, PlayerService::class.java)
        }

        fun getIntent(context: Context, episode: EpisodeItem?): Intent {
            return Intent(context, PlayerService::class.java).apply {
                putExtras(Bundle().apply {
                    putSerializable(EPISODE_ITEM_KEY, episode)
                })
            }
        }
    }
}

/**
 * A Callback for when the service is connected or disconnected. Use those callbacks to
 * handle UI changes when the service is connected or disconnected
 */
interface ConnectionCallback {
    /**
     * Called when the service is connected
     */
    fun onPlayerServiceConnected()

    /**
     * Called when the player state changed
     */
    fun notifyPlayerState(state: PlayerStates, episodeItem: EpisodeItem?)

    /**
     * Called when playing item has ended
     */
    fun notifyPlayEnded(episodeItem: EpisodeItem?)

    /**
     * Called when the player state has a non locally recoverable playback failure
     */
    fun notifyPlayerError(error: Throwable, episodeItem: EpisodeItem?)

    /**
     * Called when the service is disconnected
     */
    fun onPlayerServiceDisconnected()
}

enum class PlayerStates {
    NOTHING, PLAYING, PAUSED, LOADING;

    fun isPlaying(): Boolean = this == PLAYING
}