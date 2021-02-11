package com.nasabadiam.koopod.podcast.podcastlist

data class PodcastModel(
    val rssLink: String,
    val title: String,
    val author: String,
    val image: String,
    val description: String = "",
    val pubDate: String = "",
    val lastBuildDate: String = "",
    val link: String = "",
    val language: String = "",
    val managingEditor: String = "",
    val keywords: List<String> = listOf(),
    val type: String = "",
    val category: String = "",
    val summary: String = "",
    val episodes: List<EpisodeModel> = listOf(),
    var isSubscribed: Boolean = false
)

data class EpisodeModel(
    val id: Long = 0,
    val guid: String,
    var podcastId: Long = 0,
    val title: String,
    val subTitle: String = "",
    val summary: String = "",
    val author: String = "",
    val pubDate: String = "",
    val keywords: List<String> = listOf(),
    val link: String = "",
    val image: String,
    val description: String = "",
    val duration: String = "",
    val episodeId: Int = 0,
    val seasonId: Int = 0,
    val type: String = "",
    val downloadUrl: String,
    val streamUrl: String
)