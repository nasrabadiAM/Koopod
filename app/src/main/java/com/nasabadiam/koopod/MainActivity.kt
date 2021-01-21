package com.nasabadiam.koopod

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.viewinterop.AndroidViewBinding
import com.nasabadiam.koopod.databinding.ContentMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidViewBinding(ContentMainBinding::inflate)
        }
    }
}