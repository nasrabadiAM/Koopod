package com.nasabadiam.koopod.podcast.podcastlist

data class PodcastModel(
    val rssLink: String,
    val title: String,
    val author: String,
    val image: String,
    val description: String = ""
)