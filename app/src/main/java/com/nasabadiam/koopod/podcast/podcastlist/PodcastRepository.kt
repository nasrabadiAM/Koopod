package com.nasabadiam.koopod.podcast.podcastlist

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PodcastRepository @Inject constructor(
    private val localDataSource: PodcastLocalDataSource,
    private val rssRemoteDataSource: RssRemoteDataSource
) {

    fun getPodcasts(): Flow<Result<List<PodcastModel>>> {
        return localDataSource.getPodcasts().map { data ->
            Result.Success(data.map { it.toPodcastModel() })
        }
    }

    suspend fun subscribeToPodcast(podcast: PodcastModel): Result<PodcastModel> {
        val podcastResult = rssRemoteDataSource.getPodcast(podcast.rssLink)
        if (podcastResult is Result.Success) {
            localDataSource.insertPodcast(podcastResult.data)
            podcastResult.data.isSubscribed = true
        }
        return podcastResult
    }
}