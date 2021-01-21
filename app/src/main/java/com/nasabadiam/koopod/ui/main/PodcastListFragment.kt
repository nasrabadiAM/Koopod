package com.nasabadiam.koopod.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.nasabadiam.koopod.R

class PodcastListFragment : Fragment() {

    private val viewModel: PodcastListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            PodcastListScreen(findNavController())
        }
    }
}

@Composable
fun PodcastListScreen(navController: NavController) {
    Text("Hello World", Modifier.padding(16.dp))
    Button(
        onClick = {
            navController.navigate(R.id.toPodCastDetail)
        }, colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Red
        )
    ) {
        Text("To Podcast detail")
    }
}