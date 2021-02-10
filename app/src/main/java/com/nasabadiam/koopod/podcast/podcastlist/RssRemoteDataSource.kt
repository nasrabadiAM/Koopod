package com.nasabadiam.koopod.podcast.podcastlist

import kotlinx.coroutines.CoroutineDispatcher

class RssRemoteDataSource(
    private val rssService: RssService,
    private val iODispatcher: CoroutineDispatcher
) {
    suspend fun getPodcast(rssLink: String): Result<PodcastModel> {
        return safeApiCall(
            iODispatcher,
            apiCall = {
                rssService.getPodcastRss(rssLink)
            },
            map = {
                it.toPodcastModel()
            })
    }
}