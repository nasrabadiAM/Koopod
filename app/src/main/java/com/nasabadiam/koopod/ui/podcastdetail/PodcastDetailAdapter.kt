package com.nasabadiam.koopod.ui.podcastdetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.nasabadiam.koopod.BR
import com.nasabadiam.koopod.databinding.ListEpisodeItemBinding

class PodcastDetailAdapter : RecyclerView.Adapter<EpisodeItemViewHolder>() {

    var onItemClickListener: EpisodeItemClickListener? = null

    var items: List<EpisodeItem> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeItemViewHolder {
        return EpisodeItemViewHolder(
            ListEpisodeItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: EpisodeItemViewHolder, position: Int) {
        holder.bind(items[position], onItemClickListener)
    }

    override fun onViewRecycled(holder: EpisodeItemViewHolder) {
        holder.recycle()
        super.onViewRecycled(holder)
    }

    override fun onViewDetachedFromWindow(holder: EpisodeItemViewHolder) {
        onItemClickListener = null
        super.onViewDetachedFromWindow(holder)
    }

    override fun getItemCount(): Int = items.count()
}

class EpisodeItemViewHolder(
    private val binding: ViewDataBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(podcastItem: EpisodeItem, onItemClick: EpisodeItemClickListener?) {
        binding.setVariable(BR.itemData, podcastItem)
        binding.setVariable(BR.itemClick, onItemClick)
    }

    fun recycle() {
        binding.setVariable(BR.itemData, null)
        binding.setVariable(BR.itemClick, null)
    }
}

interface EpisodeItemClickListener {
    fun onItemClick(item: EpisodeItem)
}