package com.nasabadiam.koopod.ui.podcastdetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.nasabadiam.koopod.BR
import com.nasabadiam.koopod.databinding.ListEpisodeItemBinding
import com.nasabadiam.koopod.ui.player.PlayerStates
import com.nasabadiam.koopod.utils.DataBindingComponent

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

    override fun onBindViewHolder(
        holder: EpisodeItemViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            payloads.forEach {
                when (it) {
                    is PodcastDetailPayload.PlayPausePayload -> {
                        holder.bindPlayPausePayload(items[position], it)
                    }
                }
            }
        }
    }

    override fun onViewRecycled(holder: EpisodeItemViewHolder) {
        holder.recycle()
        super.onViewRecycled(holder)
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

    fun bindPlayPausePayload(
        episodeItem: EpisodeItem,
        payload: PodcastDetailPayload.PlayPausePayload
    ) {
        episodeItem.playState = payload.state
        if (payload.state != PlayerStates.LOADING) {
            DataBindingComponent.setAnimateOnClick(
                (binding as ListEpisodeItemBinding).playImage,
                payload.state.isPlaying()
            )
        }
    }

    fun recycle() {
        binding.setVariable(BR.itemData, null)
        binding.setVariable(BR.itemClick, null)
    }
}

interface EpisodeItemClickListener {
    fun onItemClick(item: EpisodeItem)
}