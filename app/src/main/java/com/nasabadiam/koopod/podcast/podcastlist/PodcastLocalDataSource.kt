package com.nasabadiam.koopod.podcast.podcastlist

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PodcastLocalDataSource @Inject constructor(
    private val podcastDao: PodcastDao
) {

    fun podcasts() = podcastDao.podcasts()

    fun episodes() = podcastDao.episodes()

    suspend fun getPodcast(rssLink: String): PodcastModel? =
        podcastDao.getPodcast(rssLink)?.toPodcastModel()

    suspend fun podcast(rssLink: String): Flow<PodcastModel?> {
        return podcastDao.podcastWithEpisodes(rssLink).map { it?.toPodcastModel() }
    }

    suspend fun getPodcastsList() = podcastDao.podcastsList().map { it.toPodcastModel() }

    suspend fun insertPodcastWithEpisodes(podcastModel: PodcastModel) {
        podcastDao.insertPodcastWithEpisodes(PodcastEntity.fromPodcastModel(podcastModel))
    }

    suspend fun getRssLinkWithEpisodeGuid(episodeGuid: String): String {
        return podcastDao.getPodcastRssLinkWithEpisodeGuid(episodeGuid)
    }
}