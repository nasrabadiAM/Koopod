package com.nasabadiam.koopod.ui

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.nasabadiam.koopod.R
import com.nasabadiam.koopod.ui.player.ConnectionCallback
import com.nasabadiam.koopod.ui.player.PlayerService
import com.nasabadiam.koopod.ui.player.PlayerStates
import com.nasabadiam.koopod.ui.player.PlayerViewModel
import com.nasabadiam.koopod.ui.podcastdetail.EpisodeItem
import com.nasabadiam.koopod.utils.DataBindingComponent
import com.nasabadiam.koopod.utils.collectAsState
import com.nasabadiam.koopod.utils.isMyServiceRunning
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val playerViewModel: PlayerViewModel by viewModels()
    private var playerService: PlayerService? = null
    private var playerServiceConnection = getServiceConnection()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_main)
        val playImageView = findViewById<ImageView>(R.id.playImage)
        val playIconLoading = findViewById<ProgressBar>(R.id.playIconLoading)
        val playerView = findViewById<View>(R.id.playerView)
        val playerTitle = findViewById<TextView>(R.id.name).apply {
            isSelected = true
        }
        val playerImage = findViewById<ImageView>(R.id.image)
        playImageView.setOnClickListener {
            playerViewModel.onPlayPauseActionClicked()
        }
        collectAsState(playerViewModel.playerState) { (state, it) ->
            when (state) {
                PlayerStates.NOTHING -> playerView.visibility = View.GONE
                PlayerStates.PLAYING -> {
                    requireNotNull(it)
                    playerView.visibility = View.VISIBLE
                    playerImage.visibility = if (it.image.isEmpty()) View.GONE else View.VISIBLE
                    playerTitle.text = it.title
                    playIconLoading.visibility = View.GONE
                    DataBindingComponent.bindImageUrl(playerImage, it.image)
                    DataBindingComponent.setAnimateOnClick(playImageView, true)
                }
                PlayerStates.PAUSED -> {
                    requireNotNull(it)
                    playerView.visibility = View.VISIBLE
                    playerImage.visibility = if (it.image.isEmpty()) View.GONE else View.VISIBLE
                    playerTitle.text = it.title
                    playIconLoading.visibility = View.GONE
                    playImageView.visibility = View.VISIBLE
                    DataBindingComponent.bindImageUrl(playerImage, it.image)
                    DataBindingComponent.setAnimateOnClick(playImageView, false)
                }
                PlayerStates.LOADING -> {
                    requireNotNull(it)
                    playerView.visibility = View.VISIBLE
                    playerImage.visibility = if (it.image.isEmpty()) View.GONE else View.VISIBLE
                    playerTitle.text = it.title
                    playIconLoading.visibility = View.VISIBLE
                    DataBindingComponent.bindImageUrl(playerImage, it.image)
                    playerImage.visibility = View.GONE
                }
            }
        }
        collectAsState(playerViewModel.playerAction) {
            playOrPauseMedia(it)
        }

    }

    private fun playOrPauseMedia(episodeItem: EpisodeItem?) {

        if (isMyServiceRunning(PlayerService::class.java)) {
            if (playerService == null) {
                val intent: Intent = PlayerService.getIntent(this, episodeItem)
                bindService(
                    intent,
                    playerServiceConnection,
                    android.content.Context.BIND_AUTO_CREATE
                )
            }
            playerService?.playOrPauseMedia(episodeItem)
        } else {
            PlayerService.startService(this, episodeItem)

            val intent: Intent = PlayerService.getIntent(this, episodeItem)
            bindService(
                intent,
                playerServiceConnection,
                android.content.Context.BIND_AUTO_CREATE
            )
        }
    }

    override fun onResume() {
        if (isMyServiceRunning(PlayerService::class.java)) {
            val intent: Intent = PlayerService.getIntent(this)
            bindService(intent, playerServiceConnection, android.content.Context.BIND_AUTO_CREATE)
        }
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        if (playerService != null) {
            unbindService(playerServiceConnection)
        }
    }

    private fun getServiceConnection(): ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, iBinder: IBinder?) {
            playerService = (iBinder as PlayerService.PlayerBinder).getService()
            playerService?.setCallBack(getServiceConnectionCallback())
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            playerService?.removeCallback()
            playerService = null
        }

    }

    private fun getServiceConnectionCallback(): ConnectionCallback = object : ConnectionCallback {

        override fun onPlayerServiceConnected() {
            playerService?.updateCurrentState()
        }

        override fun notifyPlayerState(state: PlayerStates, episodeItem: EpisodeItem?) {
            lifecycleScope.launch {
                playerViewModel.onPlayerStateChanged(state, episodeItem)
            }
        }

        override fun notifyPlayEnded(episodeItem: EpisodeItem?) {
            lifecycleScope.launch {
                playerViewModel.onPlayerTrackEnded()
            }
        }

        override fun notifyPlayerError(error: Throwable, episodeItem: EpisodeItem?) {
            lifecycleScope.launch {
                playerViewModel.onPlayerTrackEnded()
            }
        }

        override fun onPlayerServiceDisconnected() {
            lifecycleScope.launch {
                playerViewModel.onPlayerDisconnected()
            }
        }
    }
}