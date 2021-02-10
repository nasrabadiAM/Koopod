package com.nasabadiam.koopod.podcast.podcastlist

import retrofit2.http.GET
import retrofit2.http.Url

interface RssService {
    @GET
    suspend fun getPodcastRss(@Url url: String): RssPodcastResponseDto
}