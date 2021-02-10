package com.nasabadiam.koopod.podcast.podcastlist

import javax.inject.Inject

class PodcastLocalDataSource @Inject constructor(
    private val podcastDao: PodcastDao
) {

    fun getPodcasts() = podcastDao.podcasts()

    suspend fun insertPodcast(podcastModel: PodcastModel) {
        podcastDao.insertPodcast(PodcastEntity.fromPodcastModel(podcastModel))
    }
}