package com.nasabadiam.koopod.podcast.podcastlist

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface PodcastDao {
    @Transaction
    @Query("SELECT * FROM podcast")
    fun podcasts(): Flow<List<PodcastEntity>>

    @Transaction
    @Query("SELECT * FROM podcast")
    suspend fun podcastsList(): List<PodcastEntity>

    @Transaction
    @Query("SELECT * FROM episode")
    fun episodes(): Flow<List<EpisodeEntity>>

    @Insert
    suspend fun insertPodcast(podcast: PodcastEntity)

    @Query("SELECT * FROM podcast where rss_link = :rssLink")
    suspend fun getPodcast(rssLink: String): PodcastEntity?
}
