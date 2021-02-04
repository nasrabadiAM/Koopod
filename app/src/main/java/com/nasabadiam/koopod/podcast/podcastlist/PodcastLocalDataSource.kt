package com.nasabadiam.koopod.podcast.podcastlist

import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PodcastLocalDataSource @Inject constructor(
    private val podcastDao: PodcastDao
) {

    fun getPodcasts() = podcastDao.podcasts().map { list ->
        list.map { item -> item.toPodcastModel() }
    }

    suspend fun insertPodcast(podcastEntity: PodcastEntity) {
        podcastDao.insertPodcast(podcastEntity)
    }
}
