package com.nasabadiam.koopod.ui.podcastdetail

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PodcastDetailDataBindingComponent {

    companion object {
        @BindingAdapter("app:episodeItems", "app:viewModel")
        @JvmStatic
        fun bindEpisodeItems(
            recyclerView: RecyclerView,
            episodes: List<EpisodeItem>?,
            viewModel: PodcastDetailViewModel
        ) {
            episodes?.let {
                val adapter = PodcastDetailAdapter()
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(
                    recyclerView.context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                adapter.items = episodes
                adapter.onItemClickListener = object : EpisodeItemClickListener {
                    override fun onItemClick(item: EpisodeItem) {
                        viewModel.onEpisodeItemClicked(item)
                    }
                }
            }
        }
    }
}