package com.nasabadiam.koopod.ui.podcastlist

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PodcastListDataBindingComponent {

    companion object {
        @BindingAdapter("app:podcastItems", "app:viewModel")
        @JvmStatic
        fun bindPodcastItems(
            recyclerView: RecyclerView,
            podcasts: List<PodcastItem>?,
            viewModel: PodcastListViewModel
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