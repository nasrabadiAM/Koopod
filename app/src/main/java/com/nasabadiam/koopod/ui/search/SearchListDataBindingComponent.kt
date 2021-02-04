package com.nasabadiam.koopod.ui.search

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nasabadiam.koopod.ui.podcastlist.PodcastItem
import com.nasabadiam.koopod.ui.podcastlist.PodcastItemClickListener
import com.nasabadiam.koopod.ui.podcastlist.PodcastListAdapter

class SearchListDataBindingComponent {

    companion object {
        @BindingAdapter("app:searchPodcastItems", "app:searchViewModel")
        @JvmStatic
        fun bindSearchPodcastItems(
            recyclerView: RecyclerView,
            podcasts: List<PodcastItem>?,
            viewModel: SearchViewModel
        ) {
            podcasts?.let {
                val adapter = PodcastListAdapter()
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(
                    recyclerView.context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                adapter.items = podcasts
                adapter.onItemClickListener = object : PodcastItemClickListener {
                    override fun onItemClick(item: PodcastItem) {
                        viewModel.onPodcastItemClicked(item)
                    }
                }
            }
        }
    }
}