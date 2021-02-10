package com.nasabadiam.koopod.ui.search

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchListDataBindingComponent {

    companion object {
        @BindingAdapter("app:searchPodcastItems", "app:searchViewModel")
        @JvmStatic
        fun bindSearchPodcastItems(
            recyclerView: RecyclerView,
            podcasts: List<SearchPodcastItem>?,
            viewModel: SearchViewModel
        ) {
            podcasts?.let {
                val adapter = SearchListAdapter()
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(
                    recyclerView.context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                adapter.items = podcasts
                adapter.onItemClickListener = object : PodcastItemClickListener {
                    override fun onItemClick(item: SearchPodcastItem) {
                        viewModel.onPodcastItemClicked(item)
                    }

                    override fun onSubscribeClick(item: SearchPodcastItem) {
                        viewModel.onSubscribePodcastClicked(item)
                    }
                }
            }
        }
    }
}