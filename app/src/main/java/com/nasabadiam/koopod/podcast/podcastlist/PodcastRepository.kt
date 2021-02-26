package com.nasabadiam.koopod.podcast.podcastlist

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PodcastRepository @Inject constructor(
    private val localDataSource: PodcastLocalDataSource,
    private val rssRemoteDataSource: RssRemoteDataSource
) {

    fun podcasts(): Flow<Result<List<PodcastModel>>> {
        return localDataSource.podcasts().map { data ->
            Result.Success(data.map { it.toPodcastModel() })
        }
    }

    suspend fun podcast(rssLink: String): Flow<Result<PodcastModel>> {
        return localDataSource.podcast(rssLink).map { data ->
            if (data == null) {
                Result.Error(ErrorModel.EmptyResult(NoSuchItemException()))
            } else {
                Result.Success(data)
            }
        }
    }

    fun episodes(): Flow<Result<List<EpisodeModel>>> {
        return localDataSource.episodes().map { data ->
            Result.Success(data.map { it.toEpisodeModel() })
        }
    }

    suspend fun subscribeToPodcast(podcast: PodcastModel): Flow<Result<PodcastModel>> = flow {
        if (isSubscribed(podcast.rssLink)) {
            emit(Result.Error(ErrorModel.Database(DuplicatePodcastException())))
        }
        val podcastResult = rssRemoteDataSource.getPodcast(podcast.rssLink)
        if (podcastResult is Result.Success) {
            val isSubscribed = isSubscribed(podcastResult.data.rssLink)
            if (isSubscribed) {
                emit(Result.Error<PodcastModel>(ErrorModel.Database(DuplicatePodcastException())))
            } else {
                localDataSource.insertPodcastWithEpisodes(podcastResult.data)
                podcastResult.data.isSubscribed = true
                emit(podcastResult)
            }
        } else {
            emit(podcastResult)
        }
    }

    private suspend fun isSubscribed(rssLink: String): Boolean {
        val isSubscribed = localDataSource.getPodcast(rssLink) != null
        return isSubscribed
    }

    suspend fun getRssLinkFromEpisodeGuid(episodeGuid: String): String {
        return localDataSource.getRssLinkWithEpisodeGuid(episodeGuid)
    }
}