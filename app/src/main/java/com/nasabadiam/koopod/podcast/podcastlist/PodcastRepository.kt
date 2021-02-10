package com.nasabadiam.koopod.podcast.podcastlist

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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

    suspend fun subscribeToPodcast(podcast: PodcastModel): Flow<Result<PodcastModel>> = flow {
        if (localDataSource.getPodcasts(podcast.rssLink) != null) {
            emit(
                Result.Error(
                    ErrorModel.Database(
                        Throwable(DUPLICATE_SUBSCRIBE_ERROR)
                    )
                )
            )
        }
        val podcastResult = rssRemoteDataSource.getPodcast(podcast.rssLink)
        if (podcastResult is Result.Success) {
            if (localDataSource.getPodcasts(podcastResult.data.rssLink) != null) {
                emit(
                    Result.Error<PodcastModel>(
                        ErrorModel.Database(
                            Throwable(DUPLICATE_SUBSCRIBE_ERROR)
                        )
                    )
                )
            } else {
                localDataSource.insertPodcast(podcastResult.data)
                podcastResult.data.isSubscribed = true
                emit(podcastResult)
            }
        }
    }

    companion object {
        const val DUPLICATE_SUBSCRIBE_ERROR = "Podcast has subscribed already!"

    }
}