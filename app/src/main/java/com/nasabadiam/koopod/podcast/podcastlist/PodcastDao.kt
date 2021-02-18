package com.nasabadiam.koopod.podcast.podcastlist

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Dao
interface PodcastDao {
    @Query("SELECT * FROM podcast")
    fun podcasts(): Flow<List<PodcastEntity>>

    @Query("SELECT * FROM podcast")
    suspend fun podcastsList(): List<PodcastEntity>

    @Query("SELECT * FROM episode")
    fun episodes(): Flow<List<EpisodeEntity>>

    @Insert
    suspend fun insertPodcast(podcast: PodcastEntity): Long

    @Query("SELECT * FROM podcast where rss_link = :rssLink")
    suspend fun getPodcast(rssLink: String): PodcastEntity?

    @Query("SELECT * FROM podcast where rss_link = :rssLink")
    fun podcast(rssLink: String): Flow<PodcastEntity>

    @Query("SELECT * FROM episode where podcast_id = :podcastId")
    suspend fun getEpisodes(podcastId: Long): List<EpisodeEntity>

    @Query("SELECT podcast_id FROM episode where guid = :guid")
    suspend fun getPodcastId(guid: String): Long

    @Query("SELECT rss_link FROM podcast where id = :podcastId")
    suspend fun getPodcastRssLink(podcastId: Long): String

    @Insert
    suspend fun insertEpisodesList(episodes: List<EpisodeEntity>)

    @Transaction
    suspend fun insertPodcastWithEpisodes(podcast: PodcastEntity) {
        val podcastId = insertPodcast(podcast)
        podcast.episodes.forEach { it.podcastId = podcastId }
        insertEpisodesList(podcast.episodes)
    }

    @Transaction
    suspend fun podcastWithEpisodes(rssLink: String): Flow<PodcastEntity?> = flow {
        val podcast = getPodcast(rssLink)
        val result: PodcastEntity? = if (podcast != null) {
            val episodes = getEpisodes(podcast.id)
            podcast.copy(episodes = episodes)
        } else {
            podcast
        }
        emit(result)
    }

    @Transaction
    suspend fun getPodcastRssLinkWithEpisodeGuid(guid: String): String {
        val podcastId = getPodcastId(guid)
        return getPodcastRssLink(podcastId)
    }
}
