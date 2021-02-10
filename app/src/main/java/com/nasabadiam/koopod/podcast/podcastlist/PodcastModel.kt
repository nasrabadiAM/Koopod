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
    val items: List<EpisodeModel> = listOf(),
    var isSubscribed: Boolean = false
)

data class EpisodeModel(
    val guid: String,
    val title: String,
    val subTitle: String,
    val summary: String,
    val author: String,
    val pubDate: String,
    val keywords: List<String>,
    val link: String,
    val image: String,
    val description: String,
    val duration: String,
    val episodeId: Int,
    val seasonId: Int,
    val type: String,
    val downloadUrl: String,
    val streamUrl: String
)