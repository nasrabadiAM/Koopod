package com.nasabadiam.koopod.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.nasabadiam.koopod.BR
import com.nasabadiam.koopod.databinding.ListSearchPodcastItemBinding

class SearchListAdapter : RecyclerView.Adapter<SearchPodcastItemViewHolder>() {

    var onItemClickListener: PodcastItemClickListener? = null

    var items: List<SearchPodcastItem> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchPodcastItemViewHolder {
        return SearchPodcastItemViewHolder(
            ListSearchPodcastItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchPodcastItemViewHolder, position: Int) {
        holder.bind(items[position], onItemClickListener)
    }

    override fun onBindViewHolder(
        holder: SearchPodcastItemViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            payloads.forEach {
                when (it) {
                    is SearchPodcastItem.SubscribePayLoad -> {
                        holder.bindLoadingPayload(items[position], it)
                    }
                }
            }
        }
    }

    override fun onViewRecycled(holder: SearchPodcastItemViewHolder) {
        holder.recycle()
        super.onViewRecycled(holder)
    }

    override fun onViewDetachedFromWindow(holder: SearchPodcastItemViewHolder) {
        onItemClickListener = null
        super.onViewDetachedFromWindow(holder)
    }

    override fun getItemCount(): Int = items.count()
}

class SearchPodcastItemViewHolder(
    private val binding: ViewDataBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(podcastItem: SearchPodcastItem, onItemClick: PodcastItemClickListener?) {
        binding.setVariable(BR.itemData, podcastItem)
        binding.setVariable(BR.itemClick, onItemClick)
    }

    fun bindLoadingPayload(
        searchItem: SearchPodcastItem,
        payload: SearchPodcastItem.SubscribePayLoad
    ) {
        searchItem.isLoading = payload.isLoading
        searchItem.isSubscribed = payload.isSubscribed

        binding.setVariable(BR.itemData, searchItem)
    }

    fun recycle() {
        binding.setVariable(BR.itemData, null)
        binding.setVariable(BR.itemClick, null)
    }
}

interface PodcastItemClickListener {
    fun onItemClick(item: SearchPodcastItem)
    fun onSubscribeClick(item: SearchPodcastItem)
}