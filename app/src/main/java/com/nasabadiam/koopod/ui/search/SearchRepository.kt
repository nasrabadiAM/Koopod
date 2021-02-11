package com.nasabadiam.koopod.ui.search

import com.nasabadiam.koopod.podcast.podcastlist.PodcastLocalDataSource
import com.nasabadiam.koopod.podcast.podcastlist.PodcastModel
import com.nasabadiam.koopod.podcast.podcastlist.Result
import com.nasabadiam.koopod.podcast.podcastlist.RssRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val podcastLocalDataSource: PodcastLocalDataSource,
    private val searchRemoteDataSource: SearchRemoteDataSource
) {

    suspend fun search(query: String): Flow<Result<List<PodcastModel>>> {
        val searchResult = searchRemoteDataSource.search(query).map { result ->
            if (result is Result.Success) {
                val allSubscribedPodcasts = podcastLocalDataSource.getPodcastsList()
                allSubscribedPodcasts.forEach { subscribed ->
                    val indexOf = result.data.indexOfFirst {
                        subscribed.rssLink == it.rssLink
                    }
                    if (indexOf >= 0) {
                        result.data[indexOf].isSubscribed = true
                    }
                }
            }
            result
        }
        return searchResult
    }
}