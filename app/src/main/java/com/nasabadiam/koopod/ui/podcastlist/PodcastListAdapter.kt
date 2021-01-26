package com.nasabadiam.koopod.ui.podcastlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.nasabadiam.koopod.BR
import com.nasabadiam.koopod.databinding.ListPodcastItemBinding

class PodcastListAdapter : RecyclerView.Adapter<PodcastItemViewHolder>() {

    var onItemClickListener: PodcastItemClickListener? = null

    var items: List<PodcastItem> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PodcastItemViewHolder {
        return PodcastItemViewHolder(
            ListPodcastItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PodcastItemViewHolder, position: Int) {
        holder.bind(items[position], onItemClickListener)
    }

    override fun onViewRecycled(holder: PodcastItemViewHolder) {
        holder.recycle()
        super.onViewRecycled(holder)
    }

    override fun onViewDetachedFromWindow(holder: PodcastItemViewHolder) {
        onItemClickListener = null
        super.onViewDetachedFromWindow(holder)
    }

    override fun getItemCount(): Int = items.count()
}

class PodcastItemViewHolder(
    private val binding: ViewDataBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(podcastItem: PodcastItem, onItemClick: PodcastItemClickListener?) {
        binding.setVariable(BR.itemData, podcastItem)
        binding.setVariable(BR.itemClick, onItemClick)
    }

    fun recycle() {
        binding.setVariable(BR.itemData, null)
        binding.setVariable(BR.itemClick, null)
    }
}

interface PodcastItemClickListener {
    fun onItemClick(item: PodcastItem)
}