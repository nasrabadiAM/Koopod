package com.nasabadiam.koopod.ui.podcastlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nasabadiam.koopod.databinding.FragmentPodcastListBinding
import com.nasabadiam.koopod.utils.collectAsState


class PodcastListFragment : Fragment() {

    private val viewModel: PodcastListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentPodcastListBinding.inflate(inflater, container, false).apply {
            viewModel = this@PodcastListFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectAsState(viewModel.navigation) {
            it?.let { dir ->
                findNavController().navigate(dir)
            }
        }
    }
}