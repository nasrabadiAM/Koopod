package com.nasabadiam.koopod.ui.podcastdetail

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PodcastDetailDataBindingComponent {

    companion object {
        private const val SCROLL_DELAY = 800L

        @BindingAdapter("app:episodeItems", "app:viewModel", "app:guid")
        @JvmStatic
        fun bindEpisodeItems(
            recyclerView: RecyclerView,
            episodes: List<EpisodeItem>?,
            viewModel: PodcastDetailViewModel,
            guid: java.lang.String
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

                val position = episodes.indexOfFirst { it.guid.equals(guid) }
                if (position >= 0) {
                    recyclerView.postDelayed({
                        recyclerView.smoothScrollToPosition(position)
                    }, SCROLL_DELAY)
                }

                adapter.onItemClickListener = object : EpisodeItemClickListener {
                    override fun onItemClick(item: EpisodeItem) {
                        viewModel.onEpisodeItemClicked(item)
                    }
                }
            }
        }
    }
}