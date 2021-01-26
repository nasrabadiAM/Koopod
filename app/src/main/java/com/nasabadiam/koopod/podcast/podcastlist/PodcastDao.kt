package com.nasabadiam.koopod.podcast.podcastlist

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.StateFlow

@Dao
interface PodcastDao {
    @Transaction
    @Query("SELECT * FROM podcast")
    suspend fun podcasts(): StateFlow<PodcastEntity>

    @Transaction
    @Query("SELECT * FROM episode")
    suspend fun episodes(): StateFlow<EpisodeEntity>

    @Insert
    suspend fun insertPodcast(podcast: PodcastEntity)
}
