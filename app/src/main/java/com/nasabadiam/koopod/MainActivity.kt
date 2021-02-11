package com.nasabadiam.koopod

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.nasabadiam.koopod.ui.player.PlayerViewModel
import com.nasabadiam.koopod.utils.DataBindingComponent
import com.nasabadiam.koopod.utils.collectAsState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val playerViewModel: PlayerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_main)
        val playImageView = findViewById<ImageView>(R.id.playImage)
        val playerView = findViewById<View>(R.id.playerView)
        val playerTitle = findViewById<TextView>(R.id.name)
        val playerImage = findViewById<ImageView>(R.id.image)
        playImageView.setOnClickListener {
            playerViewModel.onPlayPauseAction()
        }
        collectAsState(playerViewModel.notifyItemPaused) {
            DataBindingComponent.setAnimateOnClick(playImageView, false)
        }
        collectAsState(playerViewModel.notifyItemPlayed) {
            DataBindingComponent.setAnimateOnClick(playImageView, true)
        }
        collectAsState(playerViewModel.playerViewState) {
            if (it == null) {
                playerView.visibility = View.GONE
            } else {
                playerView.visibility = View.VISIBLE
                playerImage.visibility = if (it.image.isEmpty()) View.GONE else View.VISIBLE
                playerTitle.text = it.title
                DataBindingComponent.bindImageUrl(playerImage, it.image)
            }
        }
    }
}