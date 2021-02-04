package com.nasabadiam.koopod.ui.search

import com.google.gson.annotations.SerializedName
import com.nasabadiam.koopod.podcast.podcastlist.PodcastModel

data class SearchResponseDto(
    @SerializedName("resultCount") val resultCount: String,
    @SerializedName("results") val results: List<SearchPodcastDto>
) {
    fun toItems(): List<PodcastModel> {
        return results.mapNotNull { it.toPodcastModel() }
    }
}

data class SearchPodcastDto(
    @SerializedName("feedUrl") val rssLink: String?,
    @SerializedName("artistName") val authorName: String,
    @SerializedName("collectionName") val collectionName: String,
    @SerializedName("trackName") val trackName: String,
    @SerializedName("artworkUrl600") val image: String,
    @SerializedName("trackCount") val trackCount: Int,
    @SerializedName("primaryGenreName") val genre: String
) {
    fun toPodcastModel(): PodcastModel? {
        return if (rssLink.isNullOrEmpty()) {
            null
        } else {
            PodcastModel(
                rssLink = rssLink,
                title = collectionName,
                author = authorName,
                image = image,
                description = authorName
            )
        }
    }
}