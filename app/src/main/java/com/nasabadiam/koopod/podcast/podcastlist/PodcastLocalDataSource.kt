package com.nasabadiam.koopod.podcast.podcastlist

import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class PodcastLocalDataSource @Inject constructor(
    private val podcastDao: PodcastDao,
    ioDispatcher: CoroutineDispatcher
) {

    fun getPodcasts() = podcastDao.podcasts()
}
