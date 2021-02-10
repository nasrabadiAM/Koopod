package com.nasabadiam.koopod.podcast.podcastlist

import javax.inject.Inject

class PodcastLocalDataSource @Inject constructor(
    private val podcastDao: PodcastDao
) {

    fun getPodcasts() = podcastDao.podcasts()

    suspend fun getPodcasts(rssLink: String): PodcastModel? =
        podcastDao.getPodcast(rssLink)?.toPodcastModel()

    suspend fun getPodcastsList() = podcastDao.podcastsList().map { it.toPodcastModel() }

    suspend fun insertPodcast(podcastModel: PodcastModel) {
        podcastDao.insertPodcast(PodcastEntity.fromPodcastModel(podcastModel))
    }
}